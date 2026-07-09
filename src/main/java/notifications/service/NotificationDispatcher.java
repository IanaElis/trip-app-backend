package notifications.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import notifications.entity.Notification;
import notifications.entity.NotificationStatus;
import notifications.repository.NotificationRepository;
import notifications.sender.NotificationSender;
import notifications.sender.NotificationSenderFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NotificationDispatcher {

    @Inject
    NotificationRepository repository;
    @Inject
    NotificationSenderFactory senderFactory;

    @Scheduled(every = "1m")
    @Transactional
    void dispatch() {
        List<Notification> notifications = repository.findPendingJobsAt(Instant.now());

        for (Notification notification : notifications) {
            try {
                NotificationSender sender = senderFactory.get(notification.getChannel());
                sender.send(notification);
                notification.setStatus(NotificationStatus.SENT);
            }
            catch (Exception ex) {
                notification.setStatus(NotificationStatus.FAILED);
            }
        }
    }
}
