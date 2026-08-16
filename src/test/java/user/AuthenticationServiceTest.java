package user;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import notifications.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.dto.TokenPair;
import user.dto.request.ForgotPasswordDto;
import user.dto.request.LoginDto;
import user.dto.request.RegisterDto;
import user.dto.request.ResetPasswordDto;
import user.entity.*;
import user.mapper.UserMapper;
import user.repository.FailedAttemptRepository;
import user.repository.PasswordResetTokenRepository;
import user.repository.RefreshTokenRepository;
import user.repository.UserRepository;
import user.service.JwtService;
import user.service.RefreshTokenGenerator;
import user.service.implementation.AuthServiceImpl;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    AuthServiceImpl authService;

    @Mock
    UserRepository userRepository;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    FailedAttemptRepository failedAttemptRepository;
    @Mock
    JwtService jwtService;
    @Mock
    RefreshTokenGenerator refreshTokenGenerator;
    @Mock
    EmailService emailService;



    @Test
    void login_validCredentials() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setPasswordHash(BcryptUtil.bcryptHash("password"));
        user.setStatus(Status.ACTIVE);

        when(userRepository.findByEmail("user@test.com")).thenReturn(user);
        when(jwtService.jwtGenerator(user)).thenReturn("access-token");
        when(refreshTokenGenerator.generate()).thenReturn("refresh-token");

        TokenPair pair = authService.login(new LoginDto("user@test.com", "password"));

        assertNotNull(pair);
        assertEquals("access-token", pair.accessToken());
        assertEquals("refresh-token", pair.refreshToken());

        verify(refreshTokenRepository).persist(any(RefreshToken.class));
    }

    @Test
    void login_wrongPassword() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPasswordHash(BcryptUtil.bcryptHash("correct"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(user);
        assertThrows(
                UnauthorizedException.class, () -> authService.login(new LoginDto("user@test.com", "wrong")));
        verify(refreshTokenRepository, never()).persist((RefreshToken) any());
    }

    @Test
    void login_userDoesNotExist() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(null);
        assertThrows(
                UnauthorizedException.class,
                () -> authService.login(new LoginDto("missing@test.com", "password")));
        verify(failedAttemptRepository).persist(any(FailedAttempt.class));
    }

    @Test
    void login_userIsBlocked() {
        User user = new User();
        user.setEmail("user@test.com");
        user.setPasswordHash(BcryptUtil.bcryptHash("password"));
        user.setStatus(Status.BLOCKED);

        when(userRepository.findByEmail("user@test.com")).thenReturn(user);

        assertThrows(ForbiddenException.class, () ->
                authService.login(new LoginDto("user@test.com", "password"))
        );
        verify(jwtService, never()).jwtGenerator(any());
    }


    @Test
    void register_emailAlreadyExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(ClientErrorException.class, () -> authService.register(
                new RegisterDto("user@test.com", "password123", "username")));

        verify(userRepository, never()).persistAndFlush(any());
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(jwtService.jwtGenerator(any())).thenReturn("access-token");

        when(refreshTokenGenerator.generate()).thenReturn("refresh-token");

        TokenPair result = authService.register(
                new RegisterDto("user@test.com", "password123", "username"));

        assertNotNull(result);
        verify(userRepository).persistAndFlush(any(User.class));
        verify(refreshTokenRepository).persist((RefreshToken) any());
    }

    @Test
    void forgotPassword_generateResetToken() {
        User user = new User();
        user.setEmail("user@test.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(refreshTokenGenerator.generate()).thenReturn("reset-token");

        authService.forgotPassword(new ForgotPasswordDto(user.getEmail()));

        verify(passwordResetTokenRepository).persist(any(PasswordResetToken.class));
        verify(emailService).sendResetEmail(eq(user.getEmail()), eq("reset-token"));
    }

    @Test
    void resetPassword_tokenExpired() {
        PasswordResetToken token = mock(PasswordResetToken.class);

        when(token.isExpired()).thenReturn(true);
        when(passwordResetTokenRepository.findByTokenHash(any())).thenReturn(Optional.of(token));

        assertThrows(BadRequestException.class, () -> authService.resetPassword(
                new ResetPasswordDto("token", "newPassword")));
        verify(passwordResetTokenRepository).delete(token);
    }

    @Test
    void refresh_validToken() {
        User user = new User();
        user.setId(1L);
        user.setStatus(Status.ACTIVE);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiresAt(Instant.now().plusSeconds(60));

        when(refreshTokenRepository.findValid("refresh")).thenReturn(Optional.of(token));
        when(userRepository.findById(1L)).thenReturn(user);
        when(jwtService.jwtGenerator(user)).thenReturn("new-access");
        when(refreshTokenGenerator.generate()).thenReturn("new-refresh");

        TokenPair result = authService.refresh("refresh");

        assertEquals("new-access", result.accessToken());
        verify(refreshTokenRepository).persist((RefreshToken) any());
    }

    @Test
    void refresh_tokenExpired() {
        User user = new User();
        user.setId(1L);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiresAt(Instant.now().minusSeconds(5));

        when(refreshTokenRepository.findValid("refresh")).thenReturn(Optional.of(token));
        assertThrows(UnauthorizedException.class, () -> authService.refresh("refresh"));
        verify(jwtService, never()).jwtGenerator(any());
    }
}
