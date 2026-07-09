package travel.location.dto;

public record LocationDto(
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String name,
        String formattedAddress,
        String timezoneId
) {
}
