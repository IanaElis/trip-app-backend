package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.request.CreateTripRequest;
import travel.itinerary.dto.response.ShortTripDto;
import travel.itinerary.entity.Trip;

import java.util.List;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface TripMapper {

    @Mapping(target = "destination", ignore = true)
    Trip toTripEntity(CreateTripRequest dto);

    void updateTrip(CreateTripRequest dto, @MappingTarget Trip trip);

    travel.itinerary.dto.timeline.TripDto toTripTimelineDto(Trip trip);
    ShortTripDto toTripDto(Trip trip);

    List<ShortTripDto> toTripDtoList(List<Trip> trips);
}
