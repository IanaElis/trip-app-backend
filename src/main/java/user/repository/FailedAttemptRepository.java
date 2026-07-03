package user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import user.entity.FailedAttempt;

import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class FailedAttemptRepository implements PanacheRepository<FailedAttempt> {
    public FailedAttempt findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public void deleteOlderThan(Instant time) {
        delete("lastAttempt < ?1", time);
    }
}
