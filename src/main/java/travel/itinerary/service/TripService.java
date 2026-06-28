package travel.itinerary.service;

import travel.itinerary.dto.request.CreateTripRequest;
import travel.itinerary.dto.response.ShortTripDto;
import travel.itinerary.dto.timeline.TripDto;
import travel.itinerary.entity.Trip;

import java.util.List;

public interface TripService {
    ShortTripDto createTrip(Long userId, CreateTripRequest trip);
    TripDto updateTrip(Long userId, Long tripId, CreateTripRequest trip);
    void deleteTrip(Long userId, Long tripId);
    TripDto getTripById(Long userId, Long tripId);
    Trip getTripClassById(Long userId, Long tripId);
    List<ShortTripDto> getAllTrips(Long userId);
}
