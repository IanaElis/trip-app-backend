package travel.itnerary;

import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import travel.itinerary.dto.request.CreateTripRequest;
import travel.itinerary.dto.response.ShortTripDto;
import travel.itinerary.dto.timeline.TripDto;
import travel.itinerary.entity.Trip;
import travel.itinerary.mapper.TripMapper;
import travel.itinerary.repository.TripRepository;
import travel.itinerary.service.implementation.TripServiceImpl;
import travel.location.dto.PlaceDto;
import travel.location.entity.Place;
import travel.location.service.MapService;
import user.repository.UserRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private final Long USER_ID = 1L;
    private final Long TRIP_ID = 10L;
    Instant start;
    Instant end;
    PlaceDto destination;
    Place place;
    CreateTripRequest createTripRequest;
    Trip trip;

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

        createTripRequest = new CreateTripRequest("Summer Trip",
                "description", destination, start, end);
        trip = new Trip();
        trip.setId(TRIP_ID);
        trip.setName(createTripRequest.name());
        trip.setDescription(createTripRequest.description());
        trip.setStartDate(createTripRequest.startDate());
        trip.setEndDate(createTripRequest.endDate());
        trip.setDestination(place);
    }

    @Test
    void createTrip_success() {
        ShortTripDto dto = new ShortTripDto(TRIP_ID, trip.getName(), trip.getStartDate(),
                trip.getEndDate());

        when(tripMapper.toTripEntity(createTripRequest)).thenReturn(trip);
        when(mapService.findOrCreatePlace(destination)).thenReturn(place);
        when(tripMapper.toTripDto(trip)).thenReturn(dto);

        ShortTripDto result = tripService.createTrip(USER_ID, createTripRequest);

        assertNotNull(result);
        assertEquals(dto, result);
        assertEquals(USER_ID, trip.getUserId());
        assertEquals(place, trip.getDestination());

        verify(tripRepository).persistAndFlush(trip);
    }

    @Test
    void createTrip_nameLengthOne_success() {
        CreateTripRequest request = new CreateTripRequest("A", "Description",
                destination, Instant.now(), Instant.now().plusSeconds(60)
        );
        Trip trip = new Trip();

        ShortTripDto dto = new ShortTripDto(1L, "A", request.startDate(),
                request.endDate());

        when(tripMapper.toTripEntity(request)).thenReturn(trip);
        when(mapService.findOrCreatePlace(destination)).thenReturn(place);
        when(tripMapper.toTripDto(trip)).thenReturn(dto);

        ShortTripDto result = tripService.createTrip(USER_ID, request);

        assertEquals("A", result.name());

        verify(tripRepository).persistAndFlush(trip);
    }

    @Test
    void createTrip_placeAlreadyExists() {
        when(tripMapper.toTripEntity(createTripRequest)).thenReturn(trip);
        when(mapService.findOrCreatePlace(destination)).thenReturn(place);
        when(tripMapper.toTripDto(trip)).thenReturn(new ShortTripDto(TRIP_ID,
                        trip.getName(), trip.getStartDate(), trip.getEndDate()));

        tripService.createTrip(USER_ID, createTripRequest);

        verify(mapService).findOrCreatePlace(destination);

        assertEquals(place, trip.getDestination());
    }

    @Test
    void updateTrip_success() {
        Trip trip = new Trip();
        trip.setUserId(USER_ID);
        CreateTripRequest dto = createTripRequest;

        when(tripRepository.findByIdAndUserIdOptional(TRIP_ID, USER_ID)).thenReturn(Optional.of(trip));
        when(mapService.findOrCreatePlace(destination)).thenReturn(place);

        doAnswer(invocation -> {
            CreateTripRequest request = invocation.getArgument(0);
            Trip entity = invocation.getArgument(1);

            entity.setName(request.name());
            return null;
        }).when(tripMapper).updateTrip(createTripRequest, trip);

        tripService.updateTrip(USER_ID, TRIP_ID, dto);

        assertEquals(createTripRequest.name(), trip.getName());
    }

    @Test
    void updateTrip_tripDoesNotExist() {
        when(tripRepository.findByIdAndUserIdOptional(TRIP_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> tripService.updateTrip(USER_ID, TRIP_ID, createTripRequest)
        );

        verify(tripRepository, never()).persistAndFlush(any());
    }

    @Test
    void getTripById_success_mapDto() {
        TripDto dto = new TripDto(TRIP_ID, USER_ID, trip.getName(),
                trip.getDescription(), null, trip.getStartDate(), trip.getEndDate()
        );

        when(tripRepository.findByIdAndUserIdOptional(TRIP_ID, USER_ID))
                .thenReturn(Optional.of(trip));
        when(tripMapper.toTripTimelineDto(trip)).thenReturn(dto);

        TripDto result = tripService.getTripById(USER_ID, TRIP_ID);

        assertEquals(dto, result);

        verify(tripMapper).toTripTimelineDto(trip);
    }

    @Test
    void deleteTrip_success() {
        when(tripRepository.deleteByIdAndUserId(TRIP_ID, USER_ID)).thenReturn(true);

        tripService.deleteTrip(USER_ID, TRIP_ID);

        verify(tripRepository).deleteByIdAndUserId(TRIP_ID, USER_ID);
    }

    @Test
    void updateTrip_tripBelongsToAnotherUser() {
        when(tripRepository.findByIdAndUserIdOptional(TRIP_ID,2L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> tripService.updateTrip(2L, TRIP_ID, createTripRequest));
    }

    @Test
    void getAllTrips_userHasNoTrips() {
        List<Trip> trips = Collections.emptyList();
        List<ShortTripDto> dtos = Collections.emptyList();

        when(tripRepository.findAllByUserId(USER_ID)).thenReturn(trips);
        when(tripMapper.toTripDtoList(trips)).thenReturn(dtos);

        List<ShortTripDto> result = tripService.getAllTrips(USER_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(tripRepository).findAllByUserId(USER_ID);
        verify(tripMapper).toTripDtoList(trips);
    }
}
