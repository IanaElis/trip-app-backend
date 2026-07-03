package user.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import user.entity.RefreshToken;

import java.util.Optional;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshToken> {
    public Optional<RefreshToken> findValid(String token) {
        return find("token = ?1 and revoked = false", token).firstResultOptional();
    }

    public void revokeAllForUser(Long userId) {
        update("revoked = true where userId = ?1", userId);
    }
}
