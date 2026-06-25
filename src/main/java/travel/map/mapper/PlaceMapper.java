package travel.map.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import travel.map.dto.AirportDto;
import travel.map.dto.LocationDto;
import travel.map.dto.PlaceDto;
import travel.map.entity.Airport;
import travel.map.entity.Place;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PlaceMapper {
    Place toPlaceEntity(PlaceDto placeDto);

    PlaceDto toPlaceDto(Place place);
    LocationDto toLocationDto(Place place);

    Airport toAirportEntity(AirportDto dto);
    AirportDto toAirportDto(Airport entity);
}
