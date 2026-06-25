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
}
