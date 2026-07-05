package travel.itinerary.dto.response;

public sealed interface FullItineraryItemDto
permits FullAccommodationDto, FullActivityDto, FullFlightDto, FullTransportDto
{
}
