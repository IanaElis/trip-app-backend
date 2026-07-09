package travel.itinerary.dto.timeline;

import travel.location.dto.LocationDto;

public record AccommodationDto(
        LocationDto location,
        String reservationNumber
) implements TimelineDetails {
}
