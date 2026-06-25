package user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import user.entity.User;

import java.util.Optional;

//@ApplicationScoped
//public class UserRepository implements PanacheRepository<User> {
//    public Optional<User> findByUsername(String username) {
//        return find("username", username).firstResultOptional();
//    }
//}
