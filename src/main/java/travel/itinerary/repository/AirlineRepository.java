package travel.itinerary.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.itinerary.entity.carrier.Airline;

import java.util.Optional;

@ApplicationScoped
public class AirlineRepository implements PanacheRepository<Airline> {
    public Optional<Airline> findAirlineByIataCode(String iataCode){
        return find("iataCode", iataCode).firstResultOptional();
    }

    public Airline findByName(String name){
        return find("name", name).firstResult();
    }
}
