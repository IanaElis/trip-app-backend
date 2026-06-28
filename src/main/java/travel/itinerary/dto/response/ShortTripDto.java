package travel.itinerary.dto.response;

import java.time.Instant;

public record ShortTripDto(
        Long id,
        String name,
        Instant startDate,
        Instant endDate
) {
}
