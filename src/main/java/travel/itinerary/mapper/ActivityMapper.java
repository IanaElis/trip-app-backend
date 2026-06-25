package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.response.FullActivityDto;
import travel.itinerary.entity.Activity;
import travel.map.mapper.PlaceMapper;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
uses = PlaceMapper.class)
public interface ActivityMapper {

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Activity toEntity(CreateActivityRequest dto);

    void updateEntity(CreateActivityRequest dto, @MappingTarget Activity entity);

    FullActivityDto toDto(Activity dto);

}
