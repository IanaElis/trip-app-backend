package travel.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceDto(
        @NotBlank
        String googlePlaceId,

        @NotBlank
        String name,
        String address,
        String city,
        String country,

        @NotNull
        Double latitude,

        @NotNull
        Double longitude,
        String timezoneId
) {
}
