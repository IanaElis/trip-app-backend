package travel.location.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import travel.location.dto.PlaceDto;
import travel.location.entity.Airport;
import travel.location.entity.Place;
import travel.location.mapper.PlaceMapper;
import travel.location.repository.AirportRepository;
import travel.location.repository.PlaceRepository;

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
            Place newPlace = placeMapper.toPlaceEntity(placeDto);
            placeRepository.persistAndFlush(newPlace);
            return newPlace;
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
    public Airport getAirportByNameAndUpdate(PlaceDto dto) {
        if(dto.city() == null){
            throw new BadRequestException("City not found");
        }
        Airport airport = airportRepository.findByName(dto.name().trim());
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
