package travel.map.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.map.entity.Place;

@ApplicationScoped
public class PlaceRepository implements PanacheRepository<Place> {
    public Place findByGoogleId(String googleId) {
        return find("googlePlaceId", googleId).firstResult();
    }
}
