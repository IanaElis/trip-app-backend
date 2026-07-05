package notifications.service;

import notifications.dto.NotificationJobDto;
import travel.itinerary.entity.BaseItineraryItem;

public interface NotificationService {
    void scheduleNotifications(NotificationJobDto dto);
    void rescheduleNotifications(NotificationJobDto dto);
    void cancelNotifications(Long itineraryItemId);

}
