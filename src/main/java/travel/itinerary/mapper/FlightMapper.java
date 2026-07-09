package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.request.CreateFlightRequest;
import travel.itinerary.dto.response.FullFlightDto;
import travel.itinerary.entity.transport.Flight;
import travel.location.mapper.PlaceMapper;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
uses = {PlaceMapper.class, CarrierMapper.class})
public interface FlightMapper {

    @Mapping(target = "departureAirport", ignore = true)
    @Mapping(target = "arrivalAirport", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Flight toEntity(CreateFlightRequest dto);

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "departureAirport", ignore = true)
    @Mapping(target = "arrivalAirport", ignore = true)
    void updateEntity(CreateFlightRequest dto, @MappingTarget Flight entity);

    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "itemType", constant = "FLIGHT")
    FullFlightDto toDto(Flight entity);
}
