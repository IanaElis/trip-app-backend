package travel.itinerary.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import notifications.dto.NotificationDto;
import notifications.entity.TimelineItemType;
import notifications.service.NotificationService;
import travel.itinerary.dto.request.CreateAccommodationRequest;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.request.CreateFlightRequest;
import travel.itinerary.dto.request.CreateTransportRequest;
import travel.itinerary.dto.response.*;
import travel.itinerary.dto.timeline.*;
import travel.itinerary.entity.Accommodation;
import travel.itinerary.entity.Activity;
import travel.itinerary.entity.BaseItineraryItem;
import travel.itinerary.entity.Trip;
import travel.itinerary.entity.carrier.Company;
import travel.itinerary.entity.transport.Flight;
import travel.itinerary.entity.transport.Transport;
import travel.itinerary.mapper.*;
import travel.itinerary.repository.ItineraryItemRepository;
import travel.itinerary.service.CarrierService;
import travel.itinerary.service.ItineraryService;
import travel.itinerary.service.TripService;
import travel.location.entity.Airport;
import travel.location.service.MapService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ItineraryServiceImpl implements ItineraryService {
    @Inject
    ItineraryItemRepository itineraryItemRepository;

    @Inject
    TransportMapper transportMapper;
    @Inject
    FlightMapper flightMapper;
    @Inject
    AccommodationMapper accommodationMapper;
    @Inject
    ActivityMapper activityMapper;
    @Inject
    TripMapper tripMapper;
    @Inject
    TimelineMapper timelineMapper;

    @Inject
    CarrierService carrierService;
    @Inject
    MapService mapService;
    @Inject
    TripService tripService;
    @Inject
    NotificationService notificationService;

    private boolean datesOutsideTripRange(Trip trip, Instant start, Instant end) {
        return start.isBefore(trip.getStartDate()) || end.isAfter(trip.getEndDate());
    }


    @Transactional
    @Override
    public TimelineItemDto addTransport(Long userId, Long tripId,
                                        CreateTransportRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }

        Transport transport = transportMapper.toEntity(dto);
        transport.setTrip(trip);

        Company c = null;
        if(dto.companyId() == null) {
            c = carrierService.getOrCreateCompany(dto.companyName(), dto.type());
        }
        else{
            c = carrierService.getCompany(dto.companyId());
        }
        transport.setCompany(c);
        transport.setDepartureLocation(mapService.findOrCreatePlace(dto.departureLocation()));
        transport.setArrivalLocation(mapService.findOrCreatePlace(dto.arrivalLocation()));

        itineraryItemRepository.persistAndFlush(transport);
        notificationService.scheduleNotifications(
                new NotificationDto(userId, tripId, transport.getId(),
                        TimelineItemType.TRANSPORT, transport.getStartDateTime()));
        return timelineMapper.toTimelineItemDto(transport);
    }

    @Transactional
    @Override
    public TimelineItemDto addFlight(Long userId, Long tripId,
                                     CreateFlightRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }
        Flight flight = flightMapper.toEntity(dto);
        flight.setTrip(trip);

        flight.setAirline(carrierService.getAirline(dto.airlineIataCode()));
        Airport depatureAirport = null;
        Airport arrivalAirport = null;
        if(dto.departureAirport() != null) {
          depatureAirport = mapService
                    .getAirportByNameAndUpdate(dto.departureAirport());
        }
        if(dto.departureAirport() != null) {
            arrivalAirport = mapService
                    .getAirportByNameAndUpdate(dto.arrivalAirport());
        }
        flight.setDepartureAirport(depatureAirport);
        flight.setArrivalAirport(arrivalAirport);

        itineraryItemRepository.persistAndFlush(flight);
        notificationService.scheduleNotifications(
                new NotificationDto(userId, tripId, flight.getId(),
                        TimelineItemType.FLIGHT, flight.getStartDateTime()));
        return timelineMapper.toTimelineItemDto(flight);
    }

    @Transactional
    @Override
    public TimelineItemDto addAccommodation(Long userId, Long tripId, CreateAccommodationRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }
        Accommodation accommodation = accommodationMapper.toEntity(dto);

        accommodation.setTrip(trip);
        accommodation.setLocation(mapService.findOrCreatePlace(dto.location()));

        itineraryItemRepository.persistAndFlush(accommodation);

        notificationService.scheduleNotifications(
                new NotificationDto(userId, tripId, accommodation.getId(),
                        TimelineItemType.ACCOMMODATION, accommodation.getStartDateTime()));

        return timelineMapper.toTimelineItemDto(accommodation);
    }

    @Transactional
    @Override
    public TimelineItemDto addActivity(Long userId, Long tripId,
                                       CreateActivityRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }
        Activity activity = activityMapper.toEntity(dto);

        activity.setTrip(trip);
        activity.setLocation(mapService.findOrCreatePlace(dto.location()));

        itineraryItemRepository.persistAndFlush(activity);

        notificationService.scheduleNotifications(
                new NotificationDto(userId, tripId, activity.getId(),
                        TimelineItemType.ACTIVITY, activity.getStartDateTime()));
        return timelineMapper.toTimelineItemDto(activity);
    }

    @Transactional
    @Override
    public TimelineItemDto updateTransport(Long userId, Long tripId, Long id,
                                           CreateTransportRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }

        Transport entity = (Transport) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        transportMapper.updateEntity(dto, entity);
        if(dto.companyId() == null) {
            if(dto.companyName() == null) {
                throw new IllegalArgumentException("Company name is required");
            }
            Company company = carrierService
                    .getOrCreateCompany(dto.companyName(), dto.type());
            entity.setCompany(company);

        }
        else if(!dto.companyId().equals(id)) {
            Company newCompany = carrierService.getCompany(dto.companyId());
            if(newCompany == null) {
                throw new NotFoundException("Company not found");
            }
            entity.setCompany(newCompany);
        }
        entity.setDepartureLocation(mapService.findOrCreatePlace(dto.departureLocation()));
        entity.setArrivalLocation(mapService.findOrCreatePlace(dto.arrivalLocation()));

        itineraryItemRepository.persistAndFlush(entity);

        notificationService.rescheduleNotifications(
                new NotificationDto(userId, tripId, entity.getId(),
                        TimelineItemType.TRANSPORT, entity.getStartDateTime()));

        return timelineMapper.toTimelineItemDto(entity);
    }

    @Transactional
    @Override
    public TimelineItemDto updateFlight(Long userId, Long tripId, Long id,
                                        CreateFlightRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }

        Flight entity = (Flight) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        flightMapper.updateEntity(dto, entity);
        entity.setAirline(carrierService.getAirline(dto.airlineIataCode()));
        entity.setDepartureAirport(mapService
                .getAirportByNameAndUpdate(dto.departureAirport()));
        entity.setArrivalAirport(mapService
                .getAirportByNameAndUpdate(dto.arrivalAirport()));

        itineraryItemRepository.persistAndFlush(entity);

        notificationService.scheduleNotifications(
                new NotificationDto(userId, tripId, entity.getId(),
                        TimelineItemType.FLIGHT, entity.getStartDateTime()));
        return timelineMapper.toTimelineItemDto(entity);
    }

    @Transactional
    @Override
    public TimelineItemDto updateAccommodation(Long userId, Long tripId, Long id,
                                               CreateAccommodationRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }

        Accommodation entity = (Accommodation) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        accommodationMapper.updateEntity(dto, entity);

        entity.setLocation(mapService.findOrCreatePlace(dto.location()));

        itineraryItemRepository.persistAndFlush(entity);

        notificationService.rescheduleNotifications(
                new NotificationDto(userId, tripId, entity.getId(),
                        TimelineItemType.ACCOMMODATION, entity.getStartDateTime()));

        return timelineMapper.toTimelineItemDto(entity);
    }

    @Transactional
    @Override
    public TimelineItemDto updateActivity(Long userId, Long tripId, Long id,
                                          CreateActivityRequest dto) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        if(datesOutsideTripRange(trip, dto.startDateTime(), dto.endDateTime())) {
            throw new BadRequestException("Item dates must be within trip dates");
        }

        Activity entity = (Activity) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        activityMapper.updateEntity(dto, entity);

        entity.setLocation(mapService.findOrCreatePlace(dto.location()));

        itineraryItemRepository.persistAndFlush(entity);

        notificationService.scheduleNotifications(
                new NotificationDto(userId, tripId, entity.getId(),
                        TimelineItemType.ACTIVITY, entity.getStartDateTime()));

        return timelineMapper.toTimelineItemDto(entity);
    }

    @Transactional
    @Override
    public void deleteItem(Long userId, Long tripId, Long id) {
        if(!itineraryItemRepository.deleteByIdAndTripIdAndUserId(id, tripId, userId))
            throw new NotFoundException("Item not found");

        notificationService.cancelNotifications(id);
    }

    @Override
    public FullAccommodationDto getAccommodationById(Long userId, Long tripId, Long id) {
        Accommodation entity = (Accommodation) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return accommodationMapper.toAccommodationDto(entity);
    }

    @Override
    public FullFlightDto getFlightById(Long userId, Long tripId, Long id) {
        Flight entity = (Flight) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return flightMapper.toDto(entity);
    }

    @Override
    public FullTransportDto getTransportById(Long userId, Long tripId, Long id) {
        Transport entity = (Transport) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return transportMapper.toTransportDto(entity);
    }

    @Override
    public FullActivityDto getActivityById(Long userId, Long tripId, Long id) {
        Activity entity = (Activity) itineraryItemRepository
                .findByIdAndUserIdAndTripIdOptional(id, userId, tripId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return activityMapper.toDto(entity);
    }

    @Override
    public FullItineraryDto getItinerary(Long userId, Long tripId) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        List<TimelineItemDto> items = getTimelineItemsByTripId(tripId);
        return new FullItineraryDto(
                tripMapper.toTripTimelineDto(trip),
                items
        );
    }

    @Override
    public ReportDto getReport(Long userId, Long tripId) {
        Trip trip = tripService.getTripClassById(userId, tripId);
        List<FullItineraryItemDto> items = getFullItineraryItemsByTripId(tripId);
        return new ReportDto(
                tripMapper.toTripTimelineDto(trip),
                items
        );
    }

    private List<TimelineItemDto> getTimelineItemsByTripId(Long tripId) {
        List<BaseItineraryItem> itemList = itineraryItemRepository.findByTripId(tripId);
        if(itemList == null || itemList.isEmpty())
            return Collections.emptyList();
        List<TimelineItemDto> dtoList = new ArrayList<>();
        TimelineItemDto dtoItem;
        for (BaseItineraryItem item : itemList) {
            dtoItem = timelineMapper.toTimelineItemDto(item);
            dtoList.add(dtoItem);
        }
        return dtoList;
    }

    private List<FullItineraryItemDto> getFullItineraryItemsByTripId(Long tripId) {
        List<BaseItineraryItem> itemList = itineraryItemRepository.findByTripId(tripId);
        if(itemList == null || itemList.isEmpty())
            return Collections.emptyList();
        List<FullItineraryItemDto> dtoList = new ArrayList<>();
        FullItineraryItemDto dtoItem = null;
        for (BaseItineraryItem item : itemList) {
            if(item instanceof Accommodation) {
                dtoItem = accommodationMapper.toAccommodationDto((Accommodation) item);
            }
            if(item instanceof Flight) {
                dtoItem = flightMapper.toDto((Flight) item);
            }
            if(item instanceof Activity) {
                dtoItem = activityMapper.toDto((Activity) item);
            }
            if(item instanceof Transport) {
                dtoItem = transportMapper.toTransportDto((Transport) item);
            }
            dtoList.add(dtoItem);
        }
        return dtoList;
    }



}
