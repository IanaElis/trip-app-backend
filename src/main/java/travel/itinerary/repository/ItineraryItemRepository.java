package travel.itinerary.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.itinerary.entity.BaseItineraryItem;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ItineraryItemRepository implements PanacheRepository<BaseItineraryItem> {
    public List<BaseItineraryItem> findByTripId(Long tripId){
        return find("SELECT i FROM BaseItineraryItem i " +
                "WHERE i.trip.id = ?1 " +
                "ORDER BY i.startDateTime asc", tripId).list();
    }

    public Optional<BaseItineraryItem> findByIdAndUserIdAndTripIdOptional(Long id,
                                                                          Long userId, Long tripId) {
        return find("id =?1 AND trip.userId = ?2 AND trip.id = ?3", id, userId, tripId)
                .firstResultOptional();
    }

    public boolean deleteByIdAndTripIdAndUserId(Long id, Long tripId, Long userId) {
        long deleted = delete("id = ?1 AND trip.id = ?2 AND trip.userId = ?3", id, tripId, userId);
        return deleted > 0;
    }
}
