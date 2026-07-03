package travel.map.service;

import travel.map.dto.PlaceDto;
import travel.map.entity.Airport;
import travel.map.entity.Place;

public interface MapService {
    Place findOrCreatePlace(PlaceDto placeDto);
    Airport findAirportByIataCode(String iataCode);

    Airport getAirportByNameAndCityAndUpdate(PlaceDto dto);
}
