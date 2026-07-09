package notifications.dto;

import jakarta.validation.constraints.NotNull;
import notifications.entity.TimelineItemType;
import java.time.Instant;

public record NotificationDto(
        @NotNull
        Long userId,
        @NotNull
        Long tripId,
        @NotNull
        Long itineraryItemId,
        @NotNull
        TimelineItemType type,
        @NotNull
        Instant eventTime
) {
}
