package travel.map.service;

import travel.map.dto.AirportDto;
import travel.map.dto.LocationDto;
import travel.map.dto.PlaceDto;
import travel.map.entity.Airport;
import travel.map.entity.Place;

public interface MapService {
    Place findOrCreate(PlaceDto placeDto);
    Airport findByIataCode(String iataCode);
}
