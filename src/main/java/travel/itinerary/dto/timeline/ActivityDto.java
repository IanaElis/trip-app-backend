package travel.itinerary.dto.timeline;

import travel.location.dto.LocationDto;

public record ActivityDto(
        String title,
        LocationDto location
) implements TimelineDetails {
}
