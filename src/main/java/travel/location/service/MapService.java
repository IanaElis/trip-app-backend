package travel.location.service;

import travel.location.dto.PlaceDto;
import travel.location.entity.Airport;
import travel.location.entity.Place;

public interface MapService {
    Place findOrCreatePlace(PlaceDto placeDto);
    Airport findAirportByIataCode(String iataCode);

    Airport getAirportByNameAndUpdate(PlaceDto dto);
}
