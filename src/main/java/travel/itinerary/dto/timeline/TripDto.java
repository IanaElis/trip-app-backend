package travel.itinerary.dto.timeline;

import travel.map.dto.LocationDto;

import java.time.Instant;

public record TripDto(
        Long id,
        Long userId,
        String name,
        String description,
        LocationDto destination,
        Instant startDate,
        Instant endDate
) {
}
