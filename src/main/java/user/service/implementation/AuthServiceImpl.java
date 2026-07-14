package user.service.implementation;

import io.quarkus.runtime.util.HashUtil;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import notifications.service.EmailService;
import user.TooManyRequestsException;
import user.dto.*;
import user.dto.request.*;
import user.entity.*;
import user.mapper.UserMapper;
import user.repository.FailedAttemptRepository;
import user.repository.PasswordResetTokenRepository;
import user.repository.RefreshTokenRepository;
import user.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import user.service.AuthenticationService;
import user.service.JwtService;
import user.service.RefreshTokenGenerator;

import java.time.Duration;
import java.time.Instant;
import java.util.TooManyListenersException;

@ApplicationScoped
public class AuthServiceImpl implements AuthenticationService {

    @Inject
    UserRepository userRepository;
    @Inject
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Inject
    FailedAttemptRepository failedAttemptRepository;
    @Inject
    RefreshTokenRepository refreshTokenRepository;
    @Inject
    EmailService emailService;

    @Inject
    JwtService jwtService;
    @Inject
    RefreshTokenGenerator refreshTokenGenerator;
    @Inject
    UserMapper userMapper;

    private static final Duration REFRESH_TTL = Duration.ofDays(14);
    private static final int MAX_ATTEMPTS = 10;
    private static final Duration LOCK_TIME = Duration.ofMinutes(10);


    @Transactional
    @Override
    public TokenPair login(LoginDto dto){
        checkLockout(dto.email());

        User user = userRepository.findByEmail(dto.email());
        if(user == null){
            registerFailedAttempt(dto.email());
            throw new UnauthorizedException("Invalid email or password.");
        }
        if(!BcryptUtil.matches(dto.password(), user.getPasswordHash())) {
            registerFailedAttempt(dto.email());
            throw new UnauthorizedException("Invalid email or password.");
        }
        if(user.getStatus() == Status.BLOCKED) {
            throw new ForbiddenException("User account is blocked.");
        }

        clearFailedAttempts(dto.email());

        String accessToken = jwtService.jwtGenerator(user);
        String refreshToken = createRefreshToken(user);


        return new TokenPair(accessToken,refreshToken);
    }

    @Transactional
    @Override
    public TokenPair register(RegisterDto dto){
        if(userRepository.existsByEmail(dto.email())) {
            throw new ClientErrorException("Email already in use", Response.Status.CONFLICT);
        }
        if(userRepository.existsByUsername(dto.username())) {
            throw new ClientErrorException("Username already in use", Response.Status.CONFLICT);
        }

        String hashedPassword = BcryptUtil.bcryptHash(dto.password());

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPasswordHash(hashedPassword);
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(Instant.now());

        userRepository.persistAndFlush(user);

        String accessToken = jwtService.jwtGenerator(user);
        String refreshToken = createRefreshToken(user);

        return new TokenPair(accessToken,refreshToken);
    }

    @Transactional
    @Override
    public TokenPair refresh(String refreshToken) {
        RefreshToken oldToken = refreshTokenRepository.findValid(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (oldToken.getExpiresAt().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = userRepository.findById(oldToken.getUser().getId());

        if (user == null || user.getStatus() == Status.BLOCKED) {
            throw new ForbiddenException("User invalid or blocked");
        }

        oldToken.setRevoked(true);

        String newAccessToken = jwtService.jwtGenerator(user);
        String newRefreshToken = createRefreshToken(user);

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findValid(refreshToken)
                .ifPresent(token -> token.setRevoked(true));
    }

    @Override
    public UserDto currentUser(Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(user);
    }


    @Transactional
    @Override
    public void forgotPassword(ForgotPasswordDto dto){
        User user = userRepository.findByEmail(dto.email());
        if(user == null) {
            return;
        }

        String resetToken = refreshTokenGenerator.generate();
        String hashedToken = HashUtil.sha256(resetToken);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setTokenHash(hashedToken);
        prt.setExpiresAt(Instant.now().plus(Duration.ofMinutes(15)));
        prt.setCreatedAt(Instant.now());
        passwordResetTokenRepository.persist(prt);

        emailService.sendResetEmail(user.getEmail(), resetToken);

    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordDto dto){
        String receivedTokenHash = HashUtil.sha256(dto.token());

        PasswordResetToken token = passwordResetTokenRepository
                .findByTokenHash(receivedTokenHash)
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if(token.isExpired()){
            passwordResetTokenRepository.delete(token);
            throw new BadRequestException("Invalid reset token");
        }

        User user = userRepository.findByIdOptional(token.getUser().getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setPasswordHash(BcryptUtil.bcryptHash(dto.newPassword()));
        passwordResetTokenRepository.delete(token);
    }

    @Transactional
    @Override
    public UserDto updateProfile(Long userId, UpdateProfileDto dto){
        User user = userRepository.findById(userId);
        if(user == null) {
            throw new NotFoundException("User not found");
        }
        if(dto.email() != null){
            user.setEmail(dto.email());
        }
        if(dto.username() != null){
            user.setUsername(dto.username());
        }

        if (dto.currentPassword() != null || dto.newPassword() != null) {

            if (dto.currentPassword() == null || dto.newPassword() == null) {
                throw new BadRequestException("Both password fields are required");
            }
            if (!BcryptUtil.matches(dto.currentPassword(), user.getPasswordHash())) {
                throw new UnauthorizedException("Current password does not match");
            }
            user.setPasswordHash(BcryptUtil.bcryptHash(dto.newPassword()));
        }

        return userMapper.toDto(user);
    }


    private String createRefreshToken(User user) {
        String token = refreshTokenGenerator.generate();

        RefreshToken entity = new RefreshToken();
        entity.setToken(token);
        entity.setUser(user);
        entity.setExpiresAt(Instant.now().plus(REFRESH_TTL));
        entity.setCreatedAt(Instant.now());
        refreshTokenRepository.persist(entity);

        return token;
    }

    private void checkLockout(String email) {
        FailedAttempt attempt = failedAttemptRepository.findByEmail(email);
        if (attempt == null) {
            return;
        }

        Instant now = Instant.now();
        if (attempt.getFirstAttempt().plus(LOCK_TIME).isBefore(now)) {
            failedAttemptRepository.delete(attempt);
            return;
        }
        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            throw new TooManyRequestsException(
                    "Too many failed login attempts. Try again in 10 minutes.");
        }
    }

    private void registerFailedAttempt(String email) {
        Instant now = Instant.now();

        FailedAttempt attempt = failedAttemptRepository.findByEmail(email);

        if (attempt == null) {
            attempt = new FailedAttempt();
            attempt.setEmail(email);
            attempt.increment();
            attempt.setFirstAttempt(now);
            failedAttemptRepository.persist(attempt);
            return;
        }

        if (attempt.getFirstAttempt().plus(LOCK_TIME).isBefore(now)) {
            attempt.reset();
            attempt.increment();
            attempt.setFirstAttempt(now);
            return;
        }

        attempt.increment();
    }

    private void clearFailedAttempts(String email) {
        FailedAttempt attempt = failedAttemptRepository.findByEmail(email);

        if (attempt != null) {
            failedAttemptRepository.delete(attempt);
        }
    }
}
