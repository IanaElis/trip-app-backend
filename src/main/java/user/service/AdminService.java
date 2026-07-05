package user.service;

import user.dto.AdminUserDto;
import user.dto.UserDto;

import java.util.List;

public interface AdminService {
    AdminUserDto blockUser(Long id);
    AdminUserDto unblockUser(Long id);

    UserDto getUser(Long id);
    List<UserDto> getBlockedUsers();
    List<AdminUserDto> getAllUsers();
}
