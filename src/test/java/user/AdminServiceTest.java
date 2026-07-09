package user;

import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.dto.AdminUserDto;
import user.dto.UserDto;
import user.entity.Role;
import user.entity.Status;
import user.entity.User;
import user.mapper.UserMapper;
import user.repository.UserRepository;
import user.service.implementation.AdminServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @InjectMocks
    AdminServiceImpl adminService;
    @Mock
    UserMapper userMapper;
    @Mock
    UserRepository userRepository;

    @Test
    void blockUser_success(){
        User user = new User();
        user.setStatus(Status.ACTIVE);

        AdminUserDto dto = new AdminUserDto(
                1L, "test@test.com", "test", "USER", true);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));
        when(userMapper.toAdminUserDto(user)).thenReturn(dto);

        AdminUserDto result = adminService.blockUser(1L);

        assertEquals(Status.BLOCKED, user.getStatus());
        assertEquals(dto, result);

        verify(userMapper).toAdminUserDto(user);
    }

    @Test
    void blockUser_alreadyBlocked(){
        User user = new User();
        user.setStatus(Status.BLOCKED);

        AdminUserDto dto = new AdminUserDto(
                1L, "test@test.com", "test", "USER", true);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));
        when(userMapper.toAdminUserDto(user)).thenReturn(dto);

        AdminUserDto result = adminService.blockUser(1L);

        assertEquals(Status.BLOCKED, user.getStatus());
        assertEquals(dto, result);

        verify(userMapper).toAdminUserDto(user);
    }

    @Test
    void blockUser_userNotFound(){
        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adminService.blockUser(1L));

        verify(userMapper, never()).toAdminUserDto((User) any());
    }

    @Test
    void unblockUser_success(){
        User user = new User();
        user.setStatus(Status.BLOCKED);

        AdminUserDto dto = new AdminUserDto(
                1L, "test@test.com", "test", "USER", false);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));
        when(userMapper.toAdminUserDto(user)).thenReturn(dto);

        AdminUserDto result = adminService.unblockUser(1L);

        assertEquals(Status.ACTIVE, user.getStatus());
        assertEquals(dto, result);
    }

    @Test
    void unblockUser_alreadyActive(){
        User user = new User();
        user.setStatus(Status.ACTIVE);

        AdminUserDto dto = new AdminUserDto(
                1L, "test@test.com", "test", "USER", false);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));
        when(userMapper.toAdminUserDto(user)).thenReturn(dto);

        AdminUserDto result = adminService.unblockUser(1L);

        assertEquals(Status.ACTIVE, user.getStatus());
        assertEquals(dto, result);
    }

    @Test
    void unblockUser_userNotFound(){
        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adminService.unblockUser(1L));

        verify(userMapper, never()).toAdminUserDto((User) any());
    }

    @Test
    void getUser_success(){
        User user = new User();

        UserDto dto = new UserDto(1L, "test@test.com", "username", Role.ADMIN);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = adminService.getUser(1L);

        assertEquals(dto, result);
    }

    @Test
    void getUser_blockedUser_success(){
        User user = new User();
        user.setStatus(Status.BLOCKED);

        UserDto dto = new UserDto(1L, "blocked@test.com", "username", Role.USER);

        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        assertEquals(dto, adminService.getUser(1L));
    }

    @Test
    void getUser_userNotFound(){
        when(userRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adminService.getUser(1L));

        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    void getAllUsers_success(){
        User newUser = new User();
        newUser.setStatus(Status.ACTIVE);
        newUser.setUsername("test");
        newUser.setId(1L);
        newUser.setRole(Role.USER);
        newUser.setEmail("test@test.com");

        List<User> users = List.of(newUser);

        List<AdminUserDto> dto = List.of(
                new AdminUserDto(
                        1L,"test@test.com",
                        "test","USER",false));

        when(userRepository.listAll((any(Sort.class)))).thenReturn(users);
        when(userMapper.toAdminUserDto(users)).thenReturn(dto);

        List<AdminUserDto> result = adminService.getAllUsers();

        assertEquals(dto,result);
    }

    @Test
    void getAllUsers_noUsers(){
        when(userRepository.listAll((any(Sort.class))))
                .thenReturn(Collections.emptyList());
        when(userMapper.toAdminUserDto(anyList()))
                .thenReturn(Collections.emptyList());

        List<AdminUserDto> result = adminService.getAllUsers();

        assertTrue(result.isEmpty());
        verify(userRepository).listAll(any(Sort.class));
    }

    @Test
    void getAllUsers_mappingCalled(){
        List<User> users = List.of(new User());

        when(userRepository.listAll((any(Sort.class)))).thenReturn(users);
        when(userMapper.toAdminUserDto(users)).thenReturn(Collections.emptyList());

        adminService.getAllUsers();

        verify(userMapper).toAdminUserDto(users);
    }
}
