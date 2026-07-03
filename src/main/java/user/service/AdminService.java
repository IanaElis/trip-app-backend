package user.service;

import user.dto.UserDto;

import java.util.List;

public interface AdminService {
    void blockUser(Long id);
    void unblockUser(Long id);

    UserDto getUser(Long id);
    List<UserDto> getBlockedUsers();
    List<UserDto> getAllUsers();
}
