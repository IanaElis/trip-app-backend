package travel.itinerary.dto.timeline;

public sealed interface TimelineDetails
        permits TransportDto, AccommodationDto, ActivityDto, FlightDto{
}
