package travel.itinerary.entity.carrier;

import jakarta.persistence.*;

@Entity
@Table(
        name = "airlines"
//        indexes = {
//                @Index(name = "idx_airline_iata", columnList = "iata_code")
//        }
)
public class Airline{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "iata_code", length = 10)
    private String iataCode;

    public Airline(){}
    public Airline(String iataCode, String name) {
        this.name = name;
        this.iataCode = iataCode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIataCode() {
        return iataCode;
    }
}
