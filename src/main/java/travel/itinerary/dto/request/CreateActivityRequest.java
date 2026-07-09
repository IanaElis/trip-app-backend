package travel.itinerary.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import travel.location.dto.PlaceDto;

import java.time.Instant;

public record CreateActivityRequest(
        @NotNull
        Instant startDateTime,
        @NotNull
        Instant endDateTime,
        String notes,
        PlaceDto location,
        @NotBlank @Size(max = 200)
        String title,
        String description
) {
        @AssertTrue(message = "End date/time must be after start date/time")
        public boolean isDateRangeValid() {
                return startDateTime == null
                        || endDateTime == null
                        || endDateTime.isAfter(startDateTime);
        }
}
