package travel.itinerary.entity.transport;

import jakarta.persistence.*;
import travel.itinerary.entity.BaseItineraryItem;
import travel.itinerary.entity.Trip;
import travel.itinerary.entity.carrier.Company;
import travel.map.entity.Place;

import java.time.Instant;

@Entity
@Table(name = "transports")
public class Transport extends BaseItineraryItem{

    @Column(name = "confirmation_number", length = 100)
    private String confirmationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false, length = 30)
    private TransportType type;

    @ManyToOne
    @JoinColumn(name = "departure_location_id")
    private Place departureLocation;

    @ManyToOne
    @JoinColumn(name = "arrival_location_id")
    private Place arrivalLocation;

    @Column(name = "transport_identifier", length = 100)
    private String transportIdentifier;

    public Transport(){}

    public Transport(Trip trip, Instant startTime, Instant endTime,
                     String confirmationNumber, Company company,
                     TransportType type, Place departureLocation, Place arrivalLocation, String transportIdentifier) {
        super(trip, startTime, endTime);
        this.confirmationNumber = confirmationNumber;
        this.company = company;
        this.type = type;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.transportIdentifier = transportIdentifier;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public Place getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(Place departureLocation) {
        this.departureLocation = departureLocation;
    }

    public Place getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(Place arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public String getTransportIdentifier() {
        return transportIdentifier;
    }

    public void setTransportIdentifier(String transportIdentifier) {
        this.transportIdentifier = transportIdentifier;
    }
}
