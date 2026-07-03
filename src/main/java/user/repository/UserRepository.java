package user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import user.entity.Status;
import user.entity.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

     public User findByEmail(String email){
        return find("email", email).firstResult();
    }

    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }

    public boolean existsByUsername(String username) {
        return count("username", username) > 0;
    }

    public List<User> findByStatus(Status status) {
        return find("status", status).list();
    }
}
