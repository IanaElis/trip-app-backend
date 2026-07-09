package user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfileDto(
        String username,
        @Email(message = "Invalid email format")
        String email,
        @Size(min = 8, max = 64)
        String currentPassword,
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String newPassword
) {}

