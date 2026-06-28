package travel.itinerary.dto.request;


import java.time.Instant;

public record CreateFlightRequest(
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String confirmationNumber,
        String airlineIataCode,
        String departureAirportIataCode,
        String arrivalAirportIataCode,
        String flightNumber
) {
}
