package travel.itinerary.dto.response;

import travel.itinerary.dto.AirlineDto;
import travel.map.dto.AirportDto;

import java.time.Instant;

public record FullFlightDto(
        Long id,
        Long tripId,
        String itemType,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String confirmationNumber,
        AirlineDto airline,
        AirportDto departureAirport,
        AirportDto arrivalAirport,
        String flightNumber
) implements FullItineraryItemDto {
}
