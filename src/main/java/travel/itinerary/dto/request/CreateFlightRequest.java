package travel.itinerary.dto.request;


import travel.map.dto.PlaceDto;

import java.time.Instant;

public record CreateFlightRequest(
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String confirmationNumber,
        String airlineIataCode,
        PlaceDto departureAirport,
        PlaceDto arrivalAirport,
        String flightNumber
) {
}
