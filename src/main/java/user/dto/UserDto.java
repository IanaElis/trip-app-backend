package user.dto;

import user.entity.Status;

public record UserDto(
        Long id,
        String email,
        String username,
        Status status
) {
}
