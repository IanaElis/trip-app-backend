package travel.itinerary.dto.timeline;

import travel.location.dto.LocationDto;

public record TransportDto(
        String companyName,
        String confirmationNumber,
        String transportType,
        LocationDto departureLocation,
        LocationDto arrivalLocation,
        String transportId
) implements TimelineDetails {
}
