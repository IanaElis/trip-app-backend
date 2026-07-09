package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.SubclassMapping;
import travel.itinerary.dto.timeline.*;
import travel.itinerary.entity.Accommodation;
import travel.itinerary.entity.Activity;
import travel.itinerary.entity.BaseItineraryItem;
import travel.itinerary.entity.transport.Flight;
import travel.itinerary.entity.transport.Transport;
import travel.location.mapper.PlaceMapper;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = {CarrierMapper.class,
                PlaceMapper.class})
public interface TimelineMapper {

    @SubclassMapping(source = Transport.class, target = TimelineItemDto.class)
    @SubclassMapping(source = Flight.class,   target = TimelineItemDto.class)
    @SubclassMapping(source = Accommodation.class, target = TimelineItemDto.class)
    @SubclassMapping(source = Activity.class, target = TimelineItemDto.class)
    TimelineItemDto toTimelineItemDto(BaseItineraryItem item);


    @ToBaseTimelineDto
    @Mapping(target = "itemType", constant = "TRANSPORT")
    @Mapping(target = "details", source = ".")
    TimelineItemDto toTimelineItemDto(Transport t);

    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "confirmationNumber", source = "confirmationNumber")
    @Mapping(target = "transportType", source = "type")
    @Mapping(target = "departureLocation", source = "departureLocation")
    @Mapping(target = "arrivalLocation", source = "arrivalLocation")
    @Mapping(target = "transportId", source = "transportIdentifier")
    TransportDto toDetailsDto(Transport transport);

    @ToBaseTimelineDto
    @Mapping(target = "itemType", constant = "FLIGHT")
    @Mapping(target = "details", source = ".")
    TimelineItemDto toTimelineItemDto(Flight f);

    @Mapping(target = "confirmationNumber", source = "confirmationNumber")
    @Mapping(target = "airline", source = "airline")
    @Mapping(target = "departureAirport", source = "departureAirport")
    @Mapping(target = "arrivalAirport", source = "arrivalAirport")
    @Mapping(target = "flightNumber", source = "flightNumber")
    FlightDto toDetailsDto(Flight f);

    @ToBaseTimelineDto
    @Mapping(target = "itemType", constant = "ACCOMMODATION")
    @Mapping(target = "details", source = ".")
    TimelineItemDto toTimelineItemDto(Accommodation a);

    @Mapping(target = "location", source = "location")
    @Mapping(target = "reservationNumber", source = "reservationNumber")
    AccommodationDto toDetailsDto(Accommodation a);

    @ToBaseTimelineDto
    @Mapping(target = "itemType", constant = "ACTIVITY")
    @Mapping(target = "details", source = ".")
    TimelineItemDto toTimelineItemDto(Activity act);

    @Mapping(target = "location", source = "location")
    @Mapping(target = "title", source = "title")
    ActivityDto toDetailsDto(Activity a);

}
