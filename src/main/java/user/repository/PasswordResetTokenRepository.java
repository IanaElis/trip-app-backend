package user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import user.entity.PasswordResetToken;

import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PasswordResetTokenRepository
        implements PanacheRepository<PasswordResetToken> {

    public Optional<PasswordResetToken> findByTokenHash(String tokenHash) {
        return find("tokenHash", tokenHash).firstResultOptional();
    }

    public Optional<PasswordResetToken> findLatestForUser(Long userId) {
        return find("user.id = ?1 order by createdAt desc", userId).firstResultOptional();
    }

    public void deleteExpired(Instant now) {
        delete("expiresAt < ?1", now);
    }
}
