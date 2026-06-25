package travel.itinerary.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.itinerary.entity.Trip;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TripRepository implements PanacheRepository<Trip> {
    public Optional<Trip> findByIdAndUserIdOptional(Long id, Long userId) {
        return find("id =?1 AND userId = ?2", id, userId).firstResultOptional();
    }

    public boolean deleteByIdAndUserId(Long id, Long userId) {
        long deleted = delete("id = ?1 AND userId = ?2", id, userId);
        return deleted != 0;
    }

    public List<Trip> findAllByUserId(Long userId){
        return find("userId =?1 ORDER BY startDate desc", userId).list();
    }
}
