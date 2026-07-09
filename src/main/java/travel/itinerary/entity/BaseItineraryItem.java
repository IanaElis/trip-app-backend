package travel.itinerary.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "base_itinerary_items")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseItineraryItem{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "start_datetime", nullable = false)
    private Instant startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private Instant endDateTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public BaseItineraryItem() {}
    public BaseItineraryItem(Trip trip, Instant startDateTime, Instant endDateTime) {
        this.trip = trip;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
