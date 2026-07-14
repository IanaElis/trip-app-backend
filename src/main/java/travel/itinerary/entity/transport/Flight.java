package travel.itinerary.entity.transport;

import jakarta.persistence.*;
import travel.itinerary.entity.BaseItineraryItem;
import travel.itinerary.entity.Trip;
import travel.itinerary.entity.carrier.Airline;
import travel.location.entity.Airport;

import java.time.Instant;

@Entity
@Table(name = "flights")
public class Flight extends BaseItineraryItem {

    @Column(name = "confirmation_number", length = 100)
    private String confirmationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @Column(name = "flight_number", length = 20)
    private String flightNumber;

    public Flight(){}

    public Flight(Trip trip, Instant startTime, Instant endTime,
                     String confirmationNumber, Airline airline,
                     Airport departureLocation, Airport arrivalLocation,
                  String flightNumber) {
        super(trip, startTime, endTime);
        this.confirmationNumber = confirmationNumber;
        this.airline = airline;
        this.departureAirport = departureLocation;
        this.arrivalAirport = arrivalLocation;
        this.flightNumber = flightNumber;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "confirmationNumber='" + confirmationNumber + '\'' +
                ", airline=" + airline +
                ", departureAirport=" + departureAirport +
                ", arrivalAirport=" + arrivalAirport +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
