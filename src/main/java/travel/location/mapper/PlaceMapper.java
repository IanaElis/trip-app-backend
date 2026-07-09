package travel.location.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import travel.location.dto.AirportDto;
import travel.location.dto.LocationDto;
import travel.location.dto.PlaceDto;
import travel.location.entity.Airport;
import travel.location.entity.Place;

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
    @Mapping(target = "name", source = "place.name")
    @Mapping(target = "formattedAddress", source = "place.address")
    @Mapping(target = "city", source = "place.city")
    @Mapping(target = "timezoneId", source = "place.timezoneId")
    AirportDto toAirportDto(Airport entity);
}
