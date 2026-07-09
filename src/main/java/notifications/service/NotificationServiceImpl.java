package notifications.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import notifications.dto.NotificationDto;
import notifications.repository.NotificationRepository;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {
    @Inject
    NotificationGenerator generator;

    @Inject
    NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void scheduleNotifications(NotificationDto dto) {
        generator.generate(dto);
    }

    @Override
    public void rescheduleNotifications(NotificationDto dto) {
        notificationRepository.deletePendingByItemId(dto.itineraryItemId());
        generator.generate(dto);
    }

    @Override
    public void cancelNotifications(Long itineraryItemId) {
        notificationRepository.deletePendingByItemId(itineraryItemId);
    }
}
