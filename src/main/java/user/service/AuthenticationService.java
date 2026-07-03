package user.service;

import jakarta.ws.rs.core.NewCookie;
import user.dto.*;

public interface AuthenticationService {
    TokenPair login(LoginDto dto);

    TokenPair register(RegisterDto dto);

    TokenPair refresh(String refreshToken);

    void logout(String refreshToken);

    UserDto currentUser(Long userId);

    void forgotPassword(ForgotPasswordDto dto);
    boolean resetPassword(ResetPasswordDto dto);
}
