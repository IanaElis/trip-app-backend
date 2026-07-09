package travel.location.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.location.entity.Airport;

import java.util.Optional;

@ApplicationScoped
public class AirportRepository implements PanacheRepository<Airport> {
    public Optional<Airport> findByIataCode(String iataCode) {
        return find("iataCode", iataCode).firstResultOptional();
    }

    public Airport findByName(String name) {
        return find("place.name = ?1", name).firstResult();
    }
}
