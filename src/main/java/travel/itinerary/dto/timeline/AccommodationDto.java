package travel.itinerary.dto.timeline;

import travel.map.dto.LocationDto;

public record AccommodationDto(
        LocationDto location,
        String reservationNumber
) implements TimelineDetails {
}
