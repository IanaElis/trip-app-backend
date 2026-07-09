package notifications.service;

import notifications.dto.NotificationDto;

public interface NotificationService {
    void scheduleNotifications(NotificationDto dto);
    void rescheduleNotifications(NotificationDto dto);
    void cancelNotifications(Long itineraryItemId);

}
