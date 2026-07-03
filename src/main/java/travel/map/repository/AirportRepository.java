package travel.map.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.map.entity.Airport;

import java.util.Optional;

@ApplicationScoped
public class AirportRepository implements PanacheRepository<Airport> {
    public Optional<Airport> findByIataCode(String iataCode) {
        return find("iataCode", iataCode).firstResultOptional();
    }

    public Airport findByNameAndCity(String name, String city) {
        return find("place.name = ?1 AND place.city = ?2", name, city).firstResult();
    }
}
