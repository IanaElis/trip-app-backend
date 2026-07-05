package user.dto;

public record AdminUserDto(
        Long id,
        String email,
        String username,
        String role,
        boolean blocked
) {
}
