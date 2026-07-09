package travel.itnerary;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import notifications.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import travel.itinerary.dto.request.CreateAccommodationRequest;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.request.CreateFlightRequest;
import travel.itinerary.dto.request.CreateTransportRequest;
import travel.itinerary.dto.response.FullAccommodationDto;
import travel.itinerary.dto.response.FullTransportDto;
import travel.itinerary.dto.timeline.FullItineraryDto;
import travel.itinerary.dto.timeline.TimelineItemDto;
import travel.itinerary.dto.timeline.TripDto;
import travel.itinerary.entity.Accommodation;
import travel.itinerary.entity.Activity;
import travel.itinerary.entity.BaseItineraryItem;
import travel.itinerary.entity.Trip;
import travel.itinerary.entity.carrier.Airline;
import travel.itinerary.entity.carrier.Company;
import travel.itinerary.entity.carrier.CompanyType;
import travel.itinerary.entity.transport.Flight;
import travel.itinerary.entity.transport.Transport;
import travel.itinerary.entity.transport.TransportType;
import travel.itinerary.mapper.*;
import travel.itinerary.repository.ItineraryItemRepository;
import travel.itinerary.service.CarrierService;
import travel.itinerary.service.TripService;
import travel.itinerary.service.implementation.ItineraryServiceImpl;
import travel.location.dto.PlaceDto;
import travel.location.entity.Airport;
import travel.location.entity.Place;
import travel.location.service.MapService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {
    @Mock
    ItineraryItemRepository itineraryItemRepository;
    @Mock
    TransportMapper transportMapper;
    @Mock
    FlightMapper flightMapper;
    @Mock
    AccommodationMapper accommodationMapper;
    @Mock
    ActivityMapper activityMapper;
    @Mock
    TimelineMapper timelineMapper;
    @Mock
    CarrierService carrierService;
    @Mock
    TripMapper tripMapper;
    @Mock
    MapService mapService;
    @Mock
    TripService tripService;
    @Mock
    NotificationService notificationService;
    @InjectMocks
    ItineraryServiceImpl itineraryService;


    private final Long USER_ID = 1L;
    private final Long TRIP_ID = 10L;
    private final Instant START_TIME = Instant.parse("2026-08-01T00:00:00Z");
    private final Instant END_TIME = Instant.parse("2026-08-01T00:00:00Z");

    private Trip trip;
    private PlaceDto placeDto;
    private Place place;


    @BeforeEach
    void setup() {
        placeDto = new PlaceDto("google", "location", "address",
                "city", "country", 10.10, 20.20,
                "Europe/Sofia");

        place = new Place();
        place.setId(1L);
        place.setName("Location");

        trip = new Trip();
        trip.setId(TRIP_ID);
        trip.setUserId(USER_ID);
        trip.setStartDate(START_TIME);
        trip.setEndDate(END_TIME);
    }

    private CreateTransportRequest createTransportRequest() {
        return new CreateTransportRequest(START_TIME, END_TIME,
                "notes", "CONF123",
                5L, null, TransportType.BUS,
                placeDto, placeDto, "BUS-1");
    }

    private CreateAccommodationRequest createAccommodationRequest(){
        return new CreateAccommodationRequest(
                START_TIME, END_TIME, "notes", placeDto, "RES123");
    }

    private CreateActivityRequest createActivityRequest(){
        return new CreateActivityRequest(START_TIME, END_TIME,
                "notes", placeDto, "Museum", "Visit museum");
    }

    private CreateFlightRequest createFlightRequest(){
        return new CreateFlightRequest(START_TIME, END_TIME,
                "notes", "FL123", "BG",
                placeDto, placeDto, "123");
    }


    @Test
    void addTransport_success_existingCompany(){
        CreateTransportRequest dto = createTransportRequest();

        Transport transport = new Transport();
        transport.setId(1L);
        transport.setStartDateTime(dto.startDateTime());

        Company company = new Company("Bus Company", CompanyType.BUS);
        TimelineItemDto response = new TimelineItemDto(
                        1L, TRIP_ID,
                        dto.startDateTime(), dto.endDateTime(),
                        "notes", "TRANSPORT", null);

        when(tripService.getTripClassById(USER_ID, TRIP_ID)).thenReturn(trip);
        when(transportMapper.toEntity(dto)).thenReturn(transport);
        when(carrierService.getCompany(dto.companyId())).thenReturn(company);
        when(mapService.findOrCreatePlace(placeDto)).thenReturn(place);
        when(timelineMapper.toTimelineItemDto(transport)).thenReturn(response);

        TimelineItemDto result = itineraryService.addTransport(USER_ID, TRIP_ID, dto);

        assertEquals(response,result);
        assertEquals(trip, transport.getTrip());
        assertEquals(company, transport.getCompany());

        verify(itineraryItemRepository).persistAndFlush(transport);
        verify(notificationService).scheduleNotifications(any());
    }

    @Test
    void addTransport_success_createNewCompany(){
        CreateTransportRequest dto =
                new CreateTransportRequest(
                       START_TIME, END_TIME,
                        null, null,
                        null, "New Bus",
                        TransportType.BUS, placeDto, placeDto, "BUS1");

        Transport transport = new Transport();
        Company company = new Company("New Bus", CompanyType.BUS);

        when(tripService.getTripClassById(USER_ID,TRIP_ID)).thenReturn(trip);
        when(transportMapper.toEntity(dto)).thenReturn(transport);
        when(carrierService.getOrCreateCompany("New Bus", TransportType.BUS))
                .thenReturn(company);
        when(mapService.findOrCreatePlace(placeDto)).thenReturn(place);
        when(timelineMapper.toTimelineItemDto(transport)).thenReturn(null);

        itineraryService.addTransport(USER_ID, TRIP_ID, dto);

        verify(carrierService).getOrCreateCompany("New Bus", TransportType.BUS);
        verify(itineraryItemRepository).persistAndFlush(transport);
    }

    @Test
    void addTransport_datesOutsideTrip(){
        CreateTransportRequest dto = new CreateTransportRequest(
                START_TIME.minusSeconds(5),
                END_TIME.plusSeconds(1), null, null,
                1L, null, TransportType.BUS,
                placeDto, placeDto, "BUS1");

        when(tripService.getTripClassById(USER_ID,TRIP_ID)).thenReturn(trip);

        assertThrows(BadRequestException.class,
                () -> itineraryService.addTransport(USER_ID, TRIP_ID, dto));
        verify(itineraryItemRepository, never()).persistAndFlush(any());
    }

    @Test
    void addAccommodation_success() {
        CreateAccommodationRequest dto = createAccommodationRequest();
        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setStartDateTime(dto.startDateTime());

        TimelineItemDto response = new TimelineItemDto(1L, TRIP_ID,
                        dto.startDateTime(), dto.endDateTime(),
                        dto.notes(), "ACCOMMODATION", null);

        when(tripService.getTripClassById(USER_ID, TRIP_ID)).thenReturn(trip);
        when(accommodationMapper.toEntity(dto)).thenReturn(accommodation);
        when(mapService.findOrCreatePlace(placeDto)).thenReturn(place);
        when(timelineMapper.toTimelineItemDto(accommodation)).thenReturn(response);

        TimelineItemDto result =
                itineraryService.addAccommodation(USER_ID, TRIP_ID, dto);

        assertEquals(response, result);
        assertEquals(trip, accommodation.getTrip());
        assertEquals(place, accommodation.getLocation());
        verify(itineraryItemRepository).persistAndFlush(accommodation);
        verify(notificationService).scheduleNotifications(any());
    }

    @Test
    void updateAccommodation_wrongOwner(){
        CreateAccommodationRequest dto = createAccommodationRequest();

        when(tripService.getTripClassById(USER_ID, TRIP_ID)).thenReturn(trip);

        when(itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(5L, USER_ID, TRIP_ID))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itineraryService.updateAccommodation(USER_ID, TRIP_ID, 5L, dto));
        verify(accommodationMapper, never()).updateEntity(any(), any());
    }

    @Test
    void addActivity_success() {
        CreateActivityRequest dto = createActivityRequest();

        Activity activity = new Activity();
        activity.setId(1L);

        TimelineItemDto response = new TimelineItemDto(1L, TRIP_ID,
                        dto.startDateTime(), dto.endDateTime(),
                        dto.notes(), "ACTIVITY", null);

        when(tripService.getTripClassById(USER_ID, TRIP_ID)).thenReturn(trip);
        when(activityMapper.toEntity(dto)).thenReturn(activity);
        when(mapService.findOrCreatePlace(placeDto)).thenReturn(place);
        when(timelineMapper.toTimelineItemDto(activity)).thenReturn(response);

        TimelineItemDto result = itineraryService.addActivity(USER_ID, TRIP_ID, dto);

        assertEquals(response,result);
        assertEquals(trip, activity.getTrip());
        assertEquals(place, activity.getLocation());

        verify(itineraryItemRepository).persistAndFlush(activity);
        verify(notificationService).scheduleNotifications(any());
    }

    @Test
    void addFlight_success() {
        CreateFlightRequest dto = createFlightRequest();

        Flight flight = new Flight();
        flight.setId(1L);

        TimelineItemDto response = new TimelineItemDto(1L, TRIP_ID,
                        dto.startDateTime(), dto.endDateTime(), dto.notes(),
                        "FLIGHT", null);

        Airline airline = new Airline();
        airline.setName("Airline");

        Airport airport = new Airport();

        when(tripService.getTripClassById(USER_ID, TRIP_ID)).thenReturn(trip);
        when(flightMapper.toEntity(dto)).thenReturn(flight);
        when(carrierService.getAirline(dto.airlineIataCode())).thenReturn(airline);
        when(mapService.getAirportByNameAndUpdate(dto.departureAirport()))
                .thenReturn(airport);
        when(mapService.getAirportByNameAndUpdate(dto.arrivalAirport()))
                .thenReturn(airport);
        when(timelineMapper.toTimelineItemDto(flight)).thenReturn(response);

        TimelineItemDto result = itineraryService.addFlight(USER_ID, TRIP_ID, dto);

        assertEquals(response,result);
        assertEquals(trip,flight.getTrip());
        assertEquals(airline,flight.getAirline());

        verify(itineraryItemRepository).persistAndFlush(flight);
        verify(notificationService).scheduleNotifications(any());
    }

    @Test
    void addActivity_tripDoesNotExist(){
        when(tripService.getTripClassById(USER_ID,TRIP_ID))
                .thenThrow(new NotFoundException());

        assertThrows(NotFoundException.class,
                () -> itineraryService.addActivity(USER_ID, TRIP_ID, createActivityRequest()));

        verify(activityMapper, never()).toEntity(any());
    }

    @Test
    void updateAccommodation_success(){
        CreateAccommodationRequest dto = createAccommodationRequest();
        Accommodation accommodation = new Accommodation();

        accommodation.setId(5L);
        accommodation.setTrip(trip);

        TimelineItemDto response = new TimelineItemDto(5L, TRIP_ID,
                        dto.startDateTime(), dto.endDateTime(), dto.notes(),
                        "ACCOMMODATION", null);

        when(tripService.getTripClassById(USER_ID,TRIP_ID)).thenReturn(trip);
        when(itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(5L, USER_ID, TRIP_ID))
                .thenReturn(Optional.of(accommodation));
        when(mapService.findOrCreatePlace(placeDto)).thenReturn(place);
        when(timelineMapper.toTimelineItemDto(accommodation)).thenReturn(response);

        TimelineItemDto result =
                itineraryService.updateAccommodation(USER_ID, TRIP_ID, 5L, dto);

        assertEquals(response,result);

        verify(accommodationMapper).updateEntity(dto, accommodation);
        verify(notificationService).rescheduleNotifications(any());
        verify(itineraryItemRepository).persistAndFlush(accommodation);
    }

    @Test
    void deleteItem_success_cancelNotifications(){
        when(itineraryItemRepository
                .deleteByIdAndTripIdAndUserId(1L, TRIP_ID, USER_ID))
                .thenReturn(true);

        itineraryService.deleteItem(USER_ID, TRIP_ID, 1L);

        verify(itineraryItemRepository)
                .deleteByIdAndTripIdAndUserId(1L, TRIP_ID, USER_ID);
        verify(notificationService).cancelNotifications(1L);
    }

    @Test
    void getAccommodationById_success(){
        Accommodation accommodation = new Accommodation();
        FullAccommodationDto dto = new FullAccommodationDto(1L, TRIP_ID, "ACCOMMODATION",
                        Instant.now(), Instant.now().plusSeconds(3600),
                        "notes", null, "RES1");

        when(itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(1L, USER_ID, TRIP_ID))
                .thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toAccommodationDto(accommodation)).thenReturn(dto);

        FullAccommodationDto result =
                itineraryService.getAccommodationById(USER_ID, TRIP_ID, 1L);

        assertEquals(dto,result);
        verify(accommodationMapper).toAccommodationDto(accommodation);
    }

    @Test
    void getTransportById_success(){
        Transport transport = new Transport();

        FullTransportDto dto = new FullTransportDto(1L, TRIP_ID, "TRANSPORT",
                        Instant.now(), Instant.now().plusSeconds(100),
                        null, "Company", "CONF", "BUS", null, null, "BUS1");

        when(itineraryItemRepository.findByIdAndUserIdAndTripIdOptional(1L, USER_ID, TRIP_ID))
                .thenReturn(Optional.of(transport));
        when(transportMapper.toTransportDto(transport)).thenReturn(dto);

        assertEquals(dto, itineraryService.getTransportById(USER_ID, TRIP_ID, 1L));
    }
    @Test
    void getItinerary_success_empty() {

        TripDto tripDto = new TripDto(
                TRIP_ID, USER_ID, "Summer trip", "Vacation",
                null, trip.getStartDate(), trip.getEndDate());

        FullItineraryDto expected = new FullItineraryDto(tripDto, Collections.emptyList());

        when(tripService.getTripClassById(USER_ID, TRIP_ID)).thenReturn(trip);
        when(itineraryItemRepository.findByTripId(TRIP_ID)).thenReturn(Collections.emptyList());
        when(tripMapper.toTripTimelineDto(trip)).thenReturn(tripDto);

        FullItineraryDto result = itineraryService.getItinerary(USER_ID, TRIP_ID);

        assertNotNull(result);
        assertEquals(tripDto, result.trip());
        assertTrue(result.items().isEmpty());

        verify(tripService).getTripClassById(USER_ID, TRIP_ID);
        verify(itineraryItemRepository).findByTripId(TRIP_ID);
        verifyNoInteractions(timelineMapper);
    }

    @Test
    void getItinerary_success() {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setTrip(trip);

        Accommodation accommodation = new Accommodation();
        accommodation.setId(2L);
        accommodation.setTrip(trip);

        Transport transport = new Transport();
        transport.setId(3L);
        transport.setTrip(trip);

        Flight flight = new Flight();
        flight.setId(4L);
        flight.setTrip(trip);

        List<BaseItineraryItem> items = List.of(activity, accommodation, transport, flight);

        TimelineItemDto activityDto = new TimelineItemDto(
                        1L, TRIP_ID,
                        Instant.now(), Instant.now().plusSeconds(100),
                        null, "ACTIVITY", null);

        TimelineItemDto accommodationDto = new TimelineItemDto(2L, TRIP_ID,
                        Instant.now(), Instant.now().plusSeconds(100),
                        null, "ACCOMMODATION", null);

        TimelineItemDto transportDto = new TimelineItemDto(3L, TRIP_ID,
                        Instant.now(), Instant.now().plusSeconds(100),
                        null, "TRANSPORT", null);

        TimelineItemDto flightDto = new TimelineItemDto(
                        4L, TRIP_ID, Instant.now(), Instant.now().plusSeconds(100),
                        null, "FLIGHT", null);

        TripDto tripDto = new TripDto(TRIP_ID, USER_ID,
                        "Trip", null, null,
                        trip.getStartDate(), trip.getEndDate());

        when(tripService.getTripClassById(USER_ID,TRIP_ID)).thenReturn(trip);
        when(itineraryItemRepository.findByTripId(TRIP_ID)).thenReturn(items);
        when(tripMapper.toTripTimelineDto(trip)).thenReturn(tripDto);

        when(timelineMapper.toTimelineItemDto((BaseItineraryItem)activity)).thenReturn(activityDto);
        when(timelineMapper.toTimelineItemDto((BaseItineraryItem)accommodation)).thenReturn(accommodationDto);
        when(timelineMapper.toTimelineItemDto((BaseItineraryItem)transport)).thenReturn(transportDto);
        when(timelineMapper.toTimelineItemDto((BaseItineraryItem)flight)).thenReturn(flightDto);

        FullItineraryDto result = itineraryService.getItinerary(USER_ID, TRIP_ID);

        assertEquals(tripDto,result.trip());
        assertEquals(4,result.items().size());
        assertEquals("ACTIVITY", result.items().get(0).itemType());
        assertEquals("ACCOMMODATION", result.items().get(1).itemType());
        assertEquals("TRANSPORT", result.items().get(2).itemType());
        assertEquals("FLIGHT", result.items().get(3).itemType());

        verify(timelineMapper).toTimelineItemDto((BaseItineraryItem)activity);
        verify(timelineMapper).toTimelineItemDto((BaseItineraryItem)accommodation);
        verify(timelineMapper).toTimelineItemDto((BaseItineraryItem)transport);
        verify(timelineMapper).toTimelineItemDto((BaseItineraryItem)flight);
    }

}
