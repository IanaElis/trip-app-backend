package travel.itinerary.dto.request;

import travel.map.dto.PlaceDto;

import java.time.Instant;

public record CreateActivityRequest(
        Long tripId,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        PlaceDto location,
        String title,
        String description
) {
}
