package travel.itinerary.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import travel.location.dto.PlaceDto;

import java.time.Instant;

public record CreateAccommodationRequest(
        @NotNull
        Instant startDateTime,
        @NotNull
        Instant endDateTime,
        String notes,
        PlaceDto location,
        String reservationNumber
) {
        @AssertTrue(message = "End date/time must be after start date/time")
        public boolean isDateRangeValid() {
                return startDateTime == null
                        || endDateTime == null
                        || endDateTime.isAfter(startDateTime);
        }
}
