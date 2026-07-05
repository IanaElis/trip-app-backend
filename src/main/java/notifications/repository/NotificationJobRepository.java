package notifications.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import notifications.entity.NotificationJob;
import notifications.entity.NotificationStatus;

import java.time.Instant;
import java.util.List;

//@ApplicationScoped
//public class NotificationJobRepository implements PanacheRepository<NotificationJob> {
//    public void deletePendingByItemId(Long itemId) {
//        delete("itineraryItemId = ?1 and status = ?2", itemId,
//                NotificationStatus.PENDING);
//    }
//
//    public List<NotificationJob> findPendingJobsAt(Instant time) {
//        return find("sendAt = ?1 and status = ?2", time,
//                NotificationStatus.PENDING).list();
//    }
//}
