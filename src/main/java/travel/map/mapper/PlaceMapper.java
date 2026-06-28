package travel.map.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Mapping(target = "formattedAddress", source = "address")
    LocationDto toLocationDto(Place place);

    @Mapping(target = "place", ignore = true)
    Airport toAirportEntity(AirportDto dto);

    @Mapping(target = "googlePlaceId", source = "place.googlePlaceId")
    @Mapping(target = "latitude", source = "place.latitude")
    @Mapping(target = "longitude", source = "place.longitude")
    @Mapping(target = "city", source = "place.city")
    @Mapping(target = "timezoneId", source = "place.timezoneId")
    AirportDto toAirportDto(Airport entity);
}
