package notifications.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import notifications.dto.NotificationJobDto;
//import notifications.repository.NotificationJobRepository;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {
    @Inject
    NotificationGenerator generator;

//    @Inject
//    NotificationJobRepository jobRepository;

    @Transactional
    @Override
    public void scheduleNotifications(NotificationJobDto dto) {
        generator.generate(dto);
    }

    @Override
    public void rescheduleNotifications(NotificationJobDto dto) {
     //   jobRepository.deletePendingByItemId(dto.itineraryItemId());
        generator.generate(dto);
    }

    @Override
    public void cancelNotifications(Long itineraryItemId) {
      //  jobRepository.deletePendingByItemId(itineraryItemId);
    }
}
