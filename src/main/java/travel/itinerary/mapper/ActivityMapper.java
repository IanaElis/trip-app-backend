package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.response.FullActivityDto;
import travel.itinerary.entity.Activity;
import travel.location.mapper.PlaceMapper;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
uses = PlaceMapper.class)
public interface ActivityMapper {

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Activity toEntity(CreateActivityRequest dto);

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "location", ignore = true)
    void updateEntity(CreateActivityRequest dto, @MappingTarget Activity entity);

    @Mapping(target = "tripId", source = "trip.id")
    @Mapping(target = "itemType", constant = "ACTIVITY")
    @Mapping(target = "location", source = "location")
    FullActivityDto toDto(Activity dto);

}
