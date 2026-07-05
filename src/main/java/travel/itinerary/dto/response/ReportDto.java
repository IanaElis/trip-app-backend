package travel.itinerary.dto.response;

import travel.itinerary.dto.timeline.TripDto;

import java.util.List;

public record ReportDto(
        TripDto trip,
        List<FullItineraryItemDto> items
) {
}
