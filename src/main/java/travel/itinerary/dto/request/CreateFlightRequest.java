package travel.itinerary.dto.request;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import travel.location.dto.PlaceDto;

import java.time.Instant;

public record CreateFlightRequest(
        @NotNull
        Instant startDateTime,
        @NotNull
        Instant endDateTime,
        String notes,
        @Size(max = 100)
        String confirmationNumber,
        String airlineIataCode,
        @NotNull
        PlaceDto departureAirport,
        @NotNull
        PlaceDto arrivalAirport,
        @Size(max = 20)
        String flightNumber
) {
        @AssertTrue(message = "End date/time must be after start date/time")
        public boolean isDateRangeValid() {
                return startDateTime == null
                        || endDateTime == null
                        || endDateTime.isAfter(startDateTime);
        }
}
