package user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(
        @Email(message = "Invalid email format")
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String password,

        @NotBlank
        @Size(min = 2, max = 100)
        String username
) {
}
