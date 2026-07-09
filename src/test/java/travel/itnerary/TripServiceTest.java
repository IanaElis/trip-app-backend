package travel.itnerary;

import io.quarkus.security.ForbiddenException;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import travel.itinerary.dto.request.CreateTripRequest;
import travel.itinerary.entity.Trip;
import travel.itinerary.mapper.TripMapper;
import travel.itinerary.repository.TripRepository;
import travel.itinerary.service.implementation.TripServiceImpl;
import travel.location.dto.PlaceDto;
import travel.location.entity.Place;
import travel.location.service.MapService;
import user.entity.User;
import user.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static java.lang.Double.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @InjectMocks
    TripServiceImpl tripService;

    @Mock
    TripRepository tripRepository;
    @Mock
    TripMapper tripMapper;
    @Mock
    MapService mapService;
    @Mock
    UserRepository userRepository;

    Instant start;
    Instant end;
    PlaceDto destination;
    Place place;
    CreateTripRequest createTripRequest;

    @BeforeEach
    void setUp() {
        start = Instant.parse("2026-08-01T10:00:00Z");
        end = Instant.parse("2026-08-10T18:00:00Z");
        destination = new PlaceDto(
                "google","name", "address",
                "city", "country", 2.345, 3.456,
                "Europe/Paris");
        place = new Place();
        place.setGooglePlaceId("google");
        place.setName("Paris");
        place.setAddress("address");
        place.setCity("city");
        place.setCountry("country");
        place.setLatitude(2.345);
        place.setLongitude(3.456);
        place.setTimezoneId("Europe/Paris");
        createTripRequest = new CreateTripRequest("Paris",
                "description", destination, start, end);
    }

    @Test
    void createTrip_success() {
        Long userId = 1L;
        Trip trip = new Trip();

        when(tripMapper.toTripEntity(createTripRequest)).thenReturn(trip);
        when(mapService.findOrCreatePlace(destination)).thenReturn(place);

        tripService.createTrip(userId, createTripRequest);

        verify(tripRepository).persistAndFlush(any(Trip.class));
        assertEquals(userId, trip.getUserId());
    }

    @Test
    void updateTrip_success() {
        Long userId = 1L;
        Trip trip = new Trip();
        trip.setUserId(userId);
        CreateTripRequest dto = createTripRequest;

        when(tripRepository.findByIdAndUserIdOptional(5L, userId)).thenReturn(Optional.of(trip));
        when(mapService.findOrCreatePlace(destination)).thenReturn(place);

        doAnswer(invocation -> {
            CreateTripRequest request = invocation.getArgument(0);
            Trip entity = invocation.getArgument(1);

            entity.setName(request.name());
            return null;
        }).when(tripMapper).updateTrip(createTripRequest, trip);


        tripService.updateTrip(userId, 5L, dto);

        assertEquals(createTripRequest.name(), trip.getName());
    }

    @Test
    void deleteTrip_success() {
        Trip trip = new Trip();
        Long userId = 1L;
        trip.setUserId(userId);

        when(tripRepository.deleteByIdAndUserId(10L, 1L)).thenReturn(true);

        tripService.deleteTrip(userId, 10L);

        verify(tripRepository).deleteByIdAndUserId(10L,userId);
    }

    @Test
    void updateTrip_tripBelongsToAnotherUser() {
        when(tripRepository.findByIdAndUserIdOptional(1L,2L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> tripService.updateTrip(2L, 1L, createTripRequest));
    }
}
