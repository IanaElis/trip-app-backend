package user.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import user.repository.PasswordResetTokenRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ScheduledTaskService {
    @Inject
    PasswordResetTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens();
    }
}
