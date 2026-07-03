package travel.itinerary.mapper;

import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.request.CreateTransportRequest;
import travel.itinerary.dto.response.FullTransportDto;
import travel.itinerary.dto.timeline.TimelineItemDto;
import travel.itinerary.dto.timeline.TransportDto;
import travel.itinerary.entity.transport.Transport;
import travel.map.dto.LocationDto;
import travel.map.mapper.PlaceMapper;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
uses = {PlaceMapper.class, CarrierMapper.class})
public interface TransportMapper {

    @Mapping(target = "departureLocation", ignore = true)
    @Mapping(target = "arrivalLocation", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Transport toEntity(CreateTransportRequest dto);

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateEntity(CreateTransportRequest dto, @MappingTarget Transport entity);

//    @Mapping(target= "itemType", expression = "TRANSPORT")
//    @Mapping(target = "details", expression = "java(toTransportDetails(entity))")
//    TimelineItemDto toTimelineDto(Transport entity);
    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "transportType", source = "type")
    @Mapping(target = "transportId", source = "transportIdentifier")
    @Mapping(target = "itemType", constant = "TRANSPORT")
    FullTransportDto toTransportDto(Transport entity);

    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "transportType", source = "type")
    @Mapping(target = "transportId", source = "transportIdentifier")
    TransportDto toTransportDetails(Transport entity);
}
