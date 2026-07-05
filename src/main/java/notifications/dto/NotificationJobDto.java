package notifications.dto;

import notifications.entity.TimelineItemType;
import java.time.Instant;

public record NotificationJobDto(
        Long userId,
        Long tripId,
        Long itineraryItemId,
        TimelineItemType type,
        Instant eventTime
) {
}
