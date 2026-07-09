package notifications.sender;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import notifications.entity.ChannelType;
import notifications.entity.Notification;
import notifications.service.EmailService;
import user.service.UserService;


@ApplicationScoped
public class EmailSender implements NotificationSender, EmailService {

    @ConfigProperty(name = "frontend.url")
    String frontendUrl;

    @Inject
    Mailer mailer;

    @Inject
    UserService userService;

    @Override
    public ChannelType supports() {
        return ChannelType.EMAIL;
    }

    @Override
    public void send(Notification job) {
        String email = userService.getUserEmail(job.getUserId());
        mailer.send(Mail.withText(email, "Trip reminder", buildMessage(job)));
    }

    private String buildMessage(Notification job) {
        return switch (job.getItemType()) {
            case FLIGHT ->
                    "Your flight starts soon. ";
            case ACCOMMODATION ->
                    "Your hotel check-in is soon.";
            case TRANSPORT ->
                    "Your transport departs soon.";
            case ACTIVITY ->
                    "Your activity starts soon.";
        };
    }

    @Override
    public void sendResetEmail(String email, String token) {
        String link = frontendUrl + "/reset-password?token=" + token;
        String body = """
                Hello,
                Someone requested a password reset for your account.
                Click the link below to choose a new password:
                
                %s
                
                This link expires in 15 minutes.
                If you didn't request this reset, you can safely ignore this email.
                Trip Planner
                """.formatted(link);
        mailer.send(Mail.withText(email, "Trip App password reset", body));
    }

    @Override
    public boolean sendVerificationEmail() {
        return false;
    }
}
