package travel.itinerary.dto.timeline;

import travel.map.dto.LocationDto;

public record ActivityDto(
        String title,
        LocationDto location
) implements TimelineDetails {
}
