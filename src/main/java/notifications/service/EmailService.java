package notifications.service;

public interface EmailService {
    void sendResetEmail(String email, String token);
    boolean sendVerificationEmail();
}
