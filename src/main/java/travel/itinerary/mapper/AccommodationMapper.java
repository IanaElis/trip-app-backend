package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.request.CreateAccommodationRequest;
import travel.itinerary.dto.response.FullAccommodationDto;
import travel.itinerary.entity.Accommodation;
import travel.map.mapper.PlaceMapper;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
uses = PlaceMapper.class)
public interface AccommodationMapper {

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "trip", ignore = true)
    Accommodation toEntity(CreateAccommodationRequest dto);

    void updateEntity(CreateAccommodationRequest dto, @MappingTarget Accommodation entity);

    FullAccommodationDto toAccommodationDto(Accommodation entity);

}
