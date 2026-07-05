package travel.itinerary.service;

import travel.itinerary.dto.request.CreateAccommodationRequest;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.request.CreateFlightRequest;
import travel.itinerary.dto.request.CreateTransportRequest;
import travel.itinerary.dto.response.FullAccommodationDto;
import travel.itinerary.dto.response.FullActivityDto;
import travel.itinerary.dto.response.FullFlightDto;
import travel.itinerary.dto.response.FullTransportDto;
import travel.itinerary.dto.response.ReportDto;
import travel.itinerary.dto.timeline.*;

public interface ItineraryService {

    //return timeline dtos (for itinerary view)
    TimelineItemDto addTransport(Long userId, Long tripId, CreateTransportRequest dto);
    TimelineItemDto addFlight(Long userId, Long tripId, CreateFlightRequest dto);
    TimelineItemDto addAccommodation(Long userId, Long tripId, CreateAccommodationRequest dto);
    TimelineItemDto addActivity(Long userId, Long tripId, CreateActivityRequest dto);

    TimelineItemDto updateTransport(Long id, CreateTransportRequest dto);
    TimelineItemDto updateFlight(Long id, CreateFlightRequest dto);
    TimelineItemDto updateAccommodation(Long id, CreateAccommodationRequest dto);
    TimelineItemDto updateActivity(Long id, CreateActivityRequest dto);

    void deleteItem(Long id);

    //return full information on every item (item view)
    FullAccommodationDto getAccommodationById(Long id);
    FullFlightDto getFlightById(Long id);
    FullTransportDto getTransportById(Long id);
    FullActivityDto getActivityById(Long id);

    FullItineraryDto getItinerary(Long userId, Long tripId);

    ReportDto getReport(Long userId, Long tripId);
}
