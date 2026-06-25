package travel.itinerary.dto.timeline;

import java.util.List;

public record FullItineraryDto(
        TripDto trip,
        List<TimelineItemDto> items
) {
}
