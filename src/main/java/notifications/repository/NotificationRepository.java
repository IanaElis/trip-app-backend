package notifications.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import notifications.entity.Notification;
import notifications.entity.NotificationStatus;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<Notification> {
    public void deletePendingByItemId(Long itemId) {
        delete("itineraryItemId = ?1 and status = ?2", itemId,
                NotificationStatus.PENDING);
    }

    public List<Notification> findPendingJobsAt(Instant time) {
        return find("sendAt <= ?1 and status = ?2", time,
                NotificationStatus.PENDING).list();
    }
}
