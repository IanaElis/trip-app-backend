package travel.itinerary.dto.request;

import travel.itinerary.entity.transport.TransportType;
import travel.map.dto.PlaceDto;

import java.time.Instant;

public record CreateTransportRequest(
        Long tripId,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String confirmationNumber,
        Long companyId,
        String companyName,
        TransportType type,
        PlaceDto departureLocation,
        PlaceDto arrivalLocation,
        String transportIdentifier
) {
}
