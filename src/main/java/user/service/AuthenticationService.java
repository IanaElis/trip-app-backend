package user.service;

import user.dto.*;
import user.dto.request.*;

public interface AuthenticationService {
    TokenPair login(LoginDto dto);

    TokenPair register(RegisterDto dto);

    TokenPair refresh(String refreshToken);

    void logout(String refreshToken);

    UserDto currentUser(Long userId);
    UserDto updateProfile(Long userId, UpdateProfileDto dto);

    void forgotPassword(ForgotPasswordDto dto);
    void resetPassword(ResetPasswordDto dto);


}
