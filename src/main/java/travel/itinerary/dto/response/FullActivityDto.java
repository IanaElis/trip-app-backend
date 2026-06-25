package travel.itinerary.dto.response;

import java.time.Instant;

public record FullActivityDto(
        Long id,
        Long tripId,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String title,
        String description
) {
}
