package notifications.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import notifications.entity.NotificationJob;
import notifications.entity.NotificationStatus;
//import notifications.repository.NotificationJobRepository;
import notifications.sender.NotificationSender;
import notifications.sender.NotificationSenderFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NotificationDispatcher {
//
//    @Inject
//    NotificationJobRepository repository;
    @Inject
    NotificationSenderFactory senderFactory;

    @Scheduled(every = "1m")
    @Transactional
    void dispatch() {
        List<NotificationJob> jobs = new ArrayList<>();
                //repository.findPendingJobsAt(Instant.now());

        for (NotificationJob job : jobs) {
            try {
                NotificationSender sender = senderFactory.get(job.getChannel());
                sender.send(job);
                job.setStatus(NotificationStatus.SENT);
            }
            catch (Exception ex) {
                job.setStatus(NotificationStatus.FAILED);
            }
        }

    }
}
