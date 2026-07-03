package travel.itinerary.entity;

import jakarta.persistence.*;
import travel.map.entity.Place;

import java.time.Instant;

@Entity
@Table(name = "activities")
public class Activity extends BaseItineraryItem {
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Place location;

    public Activity() {}
    public Activity(Trip trip, Instant startTime, Instant endTime,
            String title, String description, Place location) {
        super(trip, startTime, endTime);
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

}
