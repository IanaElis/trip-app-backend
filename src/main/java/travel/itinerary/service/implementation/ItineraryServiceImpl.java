package travel.itinerary.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import travel.itinerary.dto.request.CreateAccommodationRequest;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.request.CreateFlightRequest;
import travel.itinerary.dto.request.CreateTransportRequest;
import travel.itinerary.dto.response.FullAccommodationDto;
import travel.itinerary.dto.response.FullActivityDto;
import travel.itinerary.dto.response.FullFlightDto;
import travel.itinerary.dto.response.FullTransportDto;
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
import travel.map.service.MapService;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
class ItineraryServiceImpl implements ItineraryService {
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


    @Transactional
    @Override
    public TimelineItemDto addTransport(Long userId, CreateTransportRequest dto) {
        Transport transport = transportMapper.toEntity(dto);

        transport.setTrip(tripService.getTripClassById(dto.tripId(), userId));

        Company c = null;
        if(dto.companyId() == null) {
            c = carrierService.createCompany(dto.companyName(), dto.type());
        }
        transport.setCompany(c);
        transport.setDepartureLocation(mapService.findOrCreate(dto.departureLocation()));
        transport.setArrivalLocation(mapService.findOrCreate(dto.arrivalLocation()));

        return timelineMapper.toTimelineItemDto(transport);
    }

    @Transactional
    @Override
    public TimelineItemDto addFlight(Long userId, CreateFlightRequest dto) {
        Flight flight = flightMapper.toEntity(dto);

        flight.setTrip(tripService.getTripClassById(dto.tripId(), userId));

        flight.setAirline(carrierService.getAirline(dto.airlineIataCode()));

        flight.setDepartureAirport(mapService.findByIataCode(dto.departureAirportIataCode()));
        flight.setArrivalAirport(mapService.findByIataCode(dto.arrivalAirportIataCode()));

        return timelineMapper.toTimelineItemDto(flight);
    }

    @Transactional
    @Override
    public TimelineItemDto addAccommodation(Long userId, CreateAccommodationRequest dto) {
        Accommodation accommodation = accommodationMapper.toEntity(dto);

        accommodation.setTrip(tripService.getTripClassById(dto.tripId(), userId));
        accommodation.setLocation(mapService.findOrCreate(dto.location()));

        return timelineMapper.toTimelineItemDto(accommodation);
    }

    @Transactional
    @Override
    public TimelineItemDto addActivity(Long userId, CreateActivityRequest dto) {
        Activity activity = activityMapper.toEntity(dto);

        activity.setTrip(tripService.getTripClassById(dto.tripId(), userId));
        activity.setLocation(mapService.findOrCreate(dto.location()));

        return timelineMapper.toTimelineItemDto(activity);
    }

    @Override
    public TimelineItemDto updateTransport(Long id, CreateTransportRequest dto) {
        Transport entity = (Transport) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        //TODO: check update
        transportMapper.updateEntity(dto, entity);
        System.out.println(entity.toString());
        return timelineMapper.toTimelineItemDto(entity);
    }

    @Override
    public TimelineItemDto updateFlight(Long id, CreateFlightRequest dto) {
        Flight entity = (Flight) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        //TODO: check update
        flightMapper.updateEntity(dto, entity);
        System.out.println(entity.toString());
        return timelineMapper.toTimelineItemDto(entity);
    }

    @Override
    public TimelineItemDto updateAccommodation(Long id,
                                                    CreateAccommodationRequest dto) {
        Accommodation entity =
                (Accommodation) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        accommodationMapper.updateEntity(dto, entity);
        System.out.println(entity.toString());
        //TODO: check update
        return timelineMapper.toTimelineItemDto(entity);
    }

    @Override
    public TimelineItemDto updateActivity(Long id, CreateActivityRequest dto) {
        Activity entity = (Activity) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        activityMapper.updateEntity(dto, entity);
        System.out.println(entity.toString());
        //TODO: check update
        return timelineMapper.toTimelineItemDto(entity);
    }

    @Override
    public void deleteItem(Long id) {
        if(!itineraryItemRepository.deleteById(id))
            throw new NotFoundException("Item not found");
    }

    @Override
    public FullAccommodationDto getAccommodationById(Long id) {
        Accommodation entity = (Accommodation) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return accommodationMapper.toAccommodationDto(entity);
    }

    @Override
    public FullFlightDto getFlightById(Long id) {
        Flight entity = (Flight) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return flightMapper.toDto(entity);
    }

    @Override
    public FullTransportDto getTransportById(Long id) {
        Transport entity = (Transport) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return transportMapper.toTransportDto(entity);
    }

    @Override
    public FullActivityDto getActivityById(Long id) {
        Activity entity = (Activity) itineraryItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        return activityMapper.toDto(entity);
    }

    @Override
    public FullItineraryDto getItinerary(Long userId, Long tripId) {
        Trip trip = tripService.getTripClassById(tripId, userId);
        List<TimelineItemDto> items = getTimelineItemsByTripId(tripId);
        return new FullItineraryDto(
                tripMapper.toTripTimelineDto(trip),
                items
        );
    }

    private List<TimelineItemDto> getTimelineItemsByTripId(Long tripId) {
        List<BaseItineraryItem> itemList = itineraryItemRepository.findByTripId(tripId);
        if(itemList == null || itemList.isEmpty())
            throw new NotFoundException("Items not found");
        List<TimelineItemDto> dtoList = new ArrayList<>();
        TimelineItemDto dtoItem;
        for (BaseItineraryItem item : itemList) {
            dtoItem = timelineMapper.toTimelineItemDto(item);
            dtoList.add(dtoItem);
        }
        return dtoList;
    }



}
