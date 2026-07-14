package travel.itinerary.dto.response;

import travel.itinerary.dto.timeline.TripDto;

import java.util.List;

public record SummaryDto(
        TripDto trip,
        List<FullItineraryItemDto> items
) {
}
