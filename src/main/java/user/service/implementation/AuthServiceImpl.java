package user.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;
import user.dto.AuthDto;
import user.entity.Role;
import user.entity.Status;
import user.entity.User;
//import user.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import user.service.AuthService;
import user.service.JwtService;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

  //  @Inject
 //   UserRepository userRepository;
    @Inject
    JwtService jwtService;

    public String login(AuthDto dto){
        User user = new User();
   //             userRepository.findByUsername(dto.username())
    //                    .orElseThrow(() -> new NotFoundException("User not found"));
        if(!BcryptUtil.matches(dto.password(), user.getPassword())) {
            throw new SecurityException("Error");
        }

        return jwtService.jwtGenerator(user.getUsername(), user.getRole(), user.getId());
    }

    public String register(AuthDto dto){
  //      if(userRepository.findByUsername(dto.username()).isPresent()) {
  //          throw new EntityExistsException("Username already exist");
   //     }

        User user = new User();
        user.setUsername(dto.username());
        String hashedPassword = BcryptUtil.bcryptHash(dto.password());
        user.setPassword(hashedPassword);
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

  //      userRepository.persistAndFlush(user);

        return jwtService.jwtGenerator(user.getUsername(), Role.USER, user.getId());
    }

}
