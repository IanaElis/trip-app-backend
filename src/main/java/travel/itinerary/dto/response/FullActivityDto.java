package travel.itinerary.dto.response;

import travel.map.dto.LocationDto;

import java.time.Instant;

public record FullActivityDto(
        Long id,
        Long tripId,
        String itemType,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String title,
        String description,
        LocationDto location
) {
}
