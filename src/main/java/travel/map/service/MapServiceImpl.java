package travel.map.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import travel.map.dto.PlaceDto;
import travel.map.entity.Airport;
import travel.map.entity.Place;
import travel.map.mapper.PlaceMapper;
import travel.map.repository.AirportRepository;
import travel.map.repository.PlaceRepository;

@ApplicationScoped
class MapServiceImpl implements MapService {
    @Inject
    PlaceRepository placeRepository;
    @Inject
    AirportRepository airportRepository;
    @Inject
    PlaceMapper placeMapper;


    @Transactional
    @Override
    public Place findOrCreatePlace(PlaceDto placeDto) {
        Place place = placeRepository.findByGoogleId(placeDto.googlePlaceId());
        if(place == null) {
            place = placeMapper.toPlaceEntity(placeDto);
            placeRepository.persistAndFlush(place);
        }
        return place;
    }

    @Override
    public Airport findAirportByIataCode(String iataCode) {
        return airportRepository.findByIataCode(iataCode)
                .orElseThrow(() -> new NotFoundException("Airport not found"));
    }

    @Transactional
    @Override
    public Airport getAirportByNameAndCityAndUpdate(PlaceDto dto) {
        Airport airport = airportRepository.findByNameAndCity(dto.name(), dto.city());
        if(airport == null) {
            throw new NotFoundException("Airport not found");
        }

        Place place = airport.getPlace();

        place.setGooglePlaceId(dto.googlePlaceId());
        place.setAddress(dto.address());
        place.setTimezoneId(dto.timezoneId());

        airportRepository.persistAndFlush(airport);
        return airport;

    }

}
