package user.dto;

import user.entity.Role;

public record UserDto(
        Long id,
        String email,
        String username,
        Role role
) {
}
