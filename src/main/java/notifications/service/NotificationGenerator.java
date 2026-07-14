package notifications.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import notifications.dto.NotificationDto;
import notifications.entity.ChannelType;
import notifications.entity.Notification;
import notifications.entity.NotificationRule;
import notifications.entity.NotificationStatus;
import notifications.repository.NotificationRepository;
import notifications.repository.NotificationRuleRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class NotificationGenerator {
    @Inject
    NotificationRuleRepository ruleRepository;

    @Inject
    NotificationRepository notificationRepository;

    @Transactional
    public void generate(NotificationDto dto) {

        List<NotificationRule> rules = ruleRepository.findByItemType(dto.type());

        for (NotificationRule rule : rules) {
            Instant sendAt = dto.eventTime()
                    .minus(rule.getOffsetMinutes(), ChronoUnit.MINUTES);
            createJob(dto, rule, sendAt);
        }
    }

    private void createJob(NotificationDto dto,
                           NotificationRule rule, Instant sendAt) {

        if (rule.isEnabled()) {
            persist(dto, sendAt, ChannelType.EMAIL);
 //           persist(dto, sendAt, ChannelType.PUSH);
 //           persist(dto, sendAt, ChannelType.IN_APP);
        }
    }

    private void persist(NotificationDto dto, Instant sendAt, ChannelType channel) {
        Notification notification = new Notification();

        notification.setUserId(dto.userId());
        notification.setTripId(dto.tripId());
        notification.setItineraryItemId(dto.itineraryItemId());
        notification.setItemType(dto.type());

        notification.setChannel(channel);
        notification.setSendAt(sendAt);
        notification.setStatus(NotificationStatus.PENDING);

        notificationRepository.persist(notification);
    }

}
