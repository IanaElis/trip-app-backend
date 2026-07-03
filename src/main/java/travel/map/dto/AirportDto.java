package travel.map.dto;

public record AirportDto(
        String googlePlaceId,
        Double latitude,
        Double longitude,
        String iataCode,
        String name,
        String formattedAddress,
        String city,
        String timezoneId
) {
}
