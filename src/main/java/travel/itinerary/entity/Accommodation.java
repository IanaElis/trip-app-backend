package travel.itinerary.entity;

import jakarta.persistence.*;
import travel.map.entity.Place;

import java.time.Instant;

@Entity
@Table(name = "accommodations")
public class Accommodation extends BaseItineraryItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Place location;

    @Column(name = "reservation_number", length = 100)
    private String reservationNumber;

    public Accommodation(){}
    public Accommodation(Trip trip, Instant startTime, Instant endTime,
                         Place location, String reservationNumber){
        super(trip, startTime, endTime);
        this.location = location;
        this.reservationNumber = reservationNumber;
    }

    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
}
