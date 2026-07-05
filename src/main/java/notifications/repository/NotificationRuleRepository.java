package notifications.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import notifications.entity.NotificationRule;
import notifications.entity.TimelineItemType;

import java.util.List;
//
//@ApplicationScoped
//public class NotificationRuleRepository implements PanacheRepository<NotificationRule> {
//    public List<NotificationRule> findByItemType(TimelineItemType itemType) {
//        return find("itemType", itemType).list();
//    }
//
//}
