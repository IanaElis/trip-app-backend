package travel.itinerary.dto.request;

import travel.map.dto.PlaceDto;

import java.time.Instant;

public record CreateTripRequest(
        String name,
        String description,
        PlaceDto destination,
        Instant startDate,
        Instant endDate
) {
}
