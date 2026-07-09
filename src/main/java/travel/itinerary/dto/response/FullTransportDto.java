package travel.itinerary.dto.response;

import travel.location.dto.LocationDto;

import java.time.Instant;

public record FullTransportDto(
        Long id,
        Long tripId,
        String itemType,
        Instant startDateTime,
        Instant endDateTime,
        String notes,
        String companyName,
        String confirmationNumber,
        String transportType,
        LocationDto departureLocation,
        LocationDto arrivalLocation,
        String transportId
) implements FullItineraryItemDto {
}
