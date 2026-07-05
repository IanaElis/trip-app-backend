package user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @Email(message = "Invalid email format")
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
