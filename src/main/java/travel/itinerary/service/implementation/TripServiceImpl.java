package travel.itinerary.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import travel.itinerary.dto.request.CreateTripRequest;
import travel.itinerary.dto.response.ShortTripDto;
import travel.itinerary.dto.timeline.TripDto;
import travel.itinerary.entity.Trip;
import travel.itinerary.mapper.TripMapper;
import travel.itinerary.repository.TripRepository;
import travel.itinerary.service.TripService;
import travel.map.service.MapService;

import java.util.List;

@ApplicationScoped
class TripServiceImpl implements TripService {
    @Inject
    TripRepository tripRepository;
    @Inject
    TripMapper tripMapper;
    @Inject
    MapService mapService;

    @Transactional
    @Override
    public ShortTripDto createTrip(Long userId, CreateTripRequest dto) {
        Trip trip = tripMapper.toTripEntity(dto);
        trip.setUserId(userId);
        trip.setDestination(mapService.findOrCreatePlace(dto.destination()));
        System.out.println(trip);
        tripRepository.persistAndFlush(trip);
        return tripMapper.toTripDto(trip);
    }

    @Transactional
    @Override
    public TripDto updateTrip(Long userId, Long tripId,
                              CreateTripRequest dto) {
        Trip oldTrip = tripRepository.findByIdAndUserIdOptional(tripId, userId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        tripMapper.updateTrip(dto, oldTrip);
        //TODO: compare old and new, update fields, check!
        System.out.println(oldTrip.toString());
        return tripMapper.toTripTimelineDto(oldTrip);
    }

    @Override
    public void deleteTrip(Long userId, Long tripId) {
        if(!tripRepository.deleteByIdAndUserId(tripId, userId)) {
            throw new NotFoundException("Trip not found");
        }
    }

    @Override
    public TripDto getTripById(Long userId, Long tripId) {
        Trip trip = tripRepository.findByIdAndUserIdOptional(tripId, userId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));
        return tripMapper.toTripTimelineDto(trip);
    }

    @Override
    public Trip getTripClassById(Long userId, Long tripId) {
        return tripRepository.findByIdAndUserIdOptional(tripId, userId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));
    }

    @Override
    public List<ShortTripDto> getAllTrips(Long userId) {
        List<Trip> trips = tripRepository.findAllByUserId(userId);
        return tripMapper.toTripDtoList(trips);
    }


}
