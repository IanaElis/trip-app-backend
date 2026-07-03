package user.service.implementation;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import user.dto.UserDto;
import user.entity.Status;
import user.entity.User;
import user.mapper.UserMapper;
import user.repository.UserRepository;
import user.service.AdminService;

import java.util.List;

@ApplicationScoped
public class AdminServiceImpl implements AdminService {
    @Inject
    UserRepository userRepository;
    @Inject
    UserMapper userMapper;

    @Override
    @Transactional
    public void blockUser(Long id) {
        User user = findUser(id);
        if(user.getStatus() == Status.BLOCKED) {
            return;
        }
        user.setStatus(Status.BLOCKED);
        //TODO: check persistence
    }

    @Transactional
    @Override
    public void unblockUser(Long id) {
        User user = findUser(id);
        if(user.getStatus() == Status.BLOCKED) {
            return;
        }
        user.setStatus(Status.ACTIVE);
        //TODO: check persistence
    }

    @Override
    public UserDto getUser(Long id) {
        User user = findUser(id);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getBlockedUsers() {
        List<User> users = userRepository.findByStatus(Status.BLOCKED);
        return userMapper.toDto(users);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.listAll(Sort.descending("createdAt"));
        return userMapper.toDto(userList);
    }

    private User findUser(Long id){
        return userRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
