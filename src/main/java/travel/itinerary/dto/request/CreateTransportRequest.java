package travel.itinerary.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import travel.itinerary.entity.transport.TransportType;
import travel.location.dto.PlaceDto;

import java.time.Instant;

public record CreateTransportRequest(
        @NotNull Instant startDateTime,
        @NotNull Instant endDateTime,
        String notes,
        @Size(max = 100) String confirmationNumber,
        Long companyId,
        String companyName,
        @NotNull TransportType type,
        @NotNull PlaceDto departureLocation,
        @NotNull PlaceDto arrivalLocation,
        @Size(max = 30) String transportIdentifier
) {
    @AssertTrue(message = "End date/time must be after start date/time")
    public boolean isDateRangeValid() {
        return startDateTime == null
                || endDateTime == null
                || endDateTime.isAfter(startDateTime);
    }
}
