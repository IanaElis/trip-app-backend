package travel.itinerary.dto.timeline;

import travel.itinerary.dto.AirlineDto;
import travel.map.dto.AirportDto;

public record FlightDto (
        String confirmationNumber,
        AirlineDto airline,
        AirportDto departureAirport,
        AirportDto arrivalAirport,
        String flightNumber
) implements TimelineDetails {
}
