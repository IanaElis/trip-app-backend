package travel.itinerary.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import travel.location.dto.PlaceDto;

import java.time.Instant;

public record CreateTripRequest(
        @NotBlank @Size(max = 200)
        String name,
        String description,

        @NotNull
        PlaceDto destination,

        @NotNull
        Instant startDate,

        @NotNull
        Instant endDate
) {
        @AssertTrue(message = "End date must be after start date")
        public boolean isDateRangeValid() {
                return startDate == null
                        || endDate == null
                        || endDate.isAfter(startDate);
        }
}
