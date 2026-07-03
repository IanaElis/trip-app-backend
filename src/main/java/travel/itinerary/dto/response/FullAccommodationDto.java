package travel.itinerary.dto.response;

import travel.map.dto.LocationDto;

import java.time.Instant;

public record FullAccommodationDto(
        Long id,
        Long tripId,
        String itemType,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        LocationDto location,
        String reservationNumber
) {
}
