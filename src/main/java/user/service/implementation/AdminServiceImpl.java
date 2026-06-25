package user.service.implementation;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import user.dto.UserDto;
import user.entity.Status;
import user.entity.User;
//import user.repository.UserRepository;
import user.service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
  //  @Inject
 //   UserRepository userRepository;

    @Override
  //  @Transactional
    public UserDto blockUser(Long id) {
        User user = findUser(id);
        user.setStatus(Status.BLOCKED);
        //TODO: mapper
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getStatus()
        );
    }

    @Override
    public UserDto getUser(Long id) {
        User user = findUser(id);
        //TODO: mapper
        return null;
    }

    @Override
    public List<UserDto> getBlockedUsers() {
        //TODO: repository method to find users by status
        //TODO: mapper
        return List.of();
    }

    @Override
    public List<UserDto> getAllUsers() {
 //       List<User> userList = userRepository.listAll(Sort.descending("createdAt"));
        //TODO: mapper
        return List.of();
    }

    private User findUser(Long id){
 //       return userRepository.findByIdOptional(id)
 //               .orElseThrow(() -> new NotFoundException("User not found"));
        return new User();
    }
}
