package travel.itinerary.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.itinerary.entity.BaseItineraryItem;

import java.util.List;

@ApplicationScoped
public class ItineraryItemRepository implements PanacheRepository<BaseItineraryItem> {
    public List<BaseItineraryItem> findByTripId(Long tripId){
        return find("SELECT i FROM BaseItineraryItem i " +
                "WHERE i.trip.id = ?1 " +
                "ORDER BY i.startDateTime asc", tripId).list();
    }

    void deleteByTripId(Long tripId){
        delete("trip.id", tripId);
    }
}
