package travel.itinerary.dto;

public record AirlineDto(
        String name,
        String iataCode,
        Double latitude,
        Double longitude,
        String formattedAddress,
        String timezoneId
) {
}
