package travel.itinerary.dto.request;

import travel.map.dto.PlaceDto;

import java.time.Instant;

public record CreateAccommodationRequest(
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        PlaceDto location,
        String reservationNumber
) {
}
