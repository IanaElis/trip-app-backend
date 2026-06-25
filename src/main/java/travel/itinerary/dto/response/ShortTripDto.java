package travel.itinerary.dto.response;

import java.time.Instant;

public record ShortTripDto(
        Long id,
        Long userId,
        String name,
        Instant startDate,
        Instant endDate
) {
}
