package travel.map.dto;

public record PlaceDto(
        String googlePlaceId,
        String name,
        String address,
        String city,
        String country,
        Double latitude,
        Double longitude,
        String timezoneId
) {
}
