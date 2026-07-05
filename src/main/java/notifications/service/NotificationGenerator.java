package notifications.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import notifications.dto.NotificationJobDto;
import notifications.entity.ChannelType;
import notifications.entity.NotificationJob;
import notifications.entity.NotificationRule;
import notifications.entity.NotificationStatus;
//import notifications.repository.NotificationJobRepository;
//import notifications.repository.NotificationRuleRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NotificationGenerator {
//    @Inject
//    NotificationRuleRepository ruleRepository;

//    @Inject
//    NotificationJobRepository jobRepository;

    @Transactional
    public void generate(NotificationJobDto dto) {

        List<NotificationRule> rules = new ArrayList<>();
                //ruleRepository.findByItemType(dto.type());

        for (NotificationRule rule : rules) {
            Instant sendAt = dto.eventTime()
                    .minus(rule.getOffsetMinutes(), ChronoUnit.MINUTES);
            createJob(dto, rule, sendAt);
        }
    }

    private void createJob(NotificationJobDto dto,
                           NotificationRule rule, Instant sendAt) {

        if (rule.isEnabled()) {
            persist(dto, sendAt, ChannelType.EMAIL);
            persist(dto, sendAt, ChannelType.PUSH);
            persist(dto, sendAt, ChannelType.IN_APP);
        }
    }

    private void persist(NotificationJobDto dto, Instant sendAt, ChannelType channel) {
        NotificationJob job = new NotificationJob();

        job.setUserId(dto.userId());
        job.setTripId(dto.tripId());
        job.setItineraryItemId(dto.itineraryItemId());
        job.setItemType(dto.type());

        job.setChannel(channel);
        job.setSendAt(sendAt);
        job.setStatus(NotificationStatus.PENDING);

   //     jobRepository.persist(job);
    }

}
