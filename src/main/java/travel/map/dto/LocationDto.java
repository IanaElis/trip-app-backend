package travel.map.dto;

public record LocationDto(
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String name,
        String formattedAddress,
        String timezoneId
) {
}
