package travel.itinerary.dto.timeline;

import java.time.Instant;

public record TimelineItemDto(
        Long id,
        Long tripId,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String itemType,
        TimelineDetails details
) {
}
