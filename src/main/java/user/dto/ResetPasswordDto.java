package user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
    @NotBlank
    String token,

    @NotBlank
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    String newPassword
) {

}
