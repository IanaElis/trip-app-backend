package travel.map.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "airports"
//        indexes = {
//                @Index(name = "idx_airport_iata", columnList = "iata_code"),
//                @Index(name = "idx_airport_icao", columnList = "icao_code")
//        }
)
public class Airport{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "iata_code", length = 10)
    private String iataCode;

    @Column(name = "icao_code", length = 10)
    private String icaoCode;

    @OneToOne(cascade = CascadeType.ALL)
    private Place place;

    public Airport(){}

    public Airport(String googlePlaceId, String name, String address,
                   String city, String country, Double latitude,
                   Double longitude, String timezoneId, String iataCode,
                   String icaoCode) {
        this.place = new Place(googlePlaceId, name, address, city,
                country, latitude, longitude, timezoneId);
        this.iataCode = iataCode;
        this.icaoCode = icaoCode;
    }

    public Long getId() {
        return id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }
}
