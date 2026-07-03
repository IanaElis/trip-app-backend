package travel.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import travel.itinerary.entity.carrier.Airline;
import travel.itinerary.repository.AirlineRepository;
import travel.map.entity.Airport;
import travel.map.entity.Place;
import travel.map.repository.AirportRepository;
import travel.map.repository.PlaceRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ImportService {
    @Inject
    PlaceRepository placeRepository;
    @Inject
    AirportRepository airportRepository;
    @Inject
    AirlineRepository airlineRepository;
    @Inject
    CountryLoader countryLoader;

    @Transactional
    public void importAirports() throws IOException {
        Map<String, String> countries = countryLoader.loadCountries();

        try (
                InputStream is = getClass()
                        .getResourceAsStream("/data/airports.csv");

                Reader reader = new InputStreamReader(is);
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .get()
                        .parse(reader)
        ) {
            for (CSVRecord row : parser) {
                importAirport(row, countries);
            }
        }
    }

    private void importAirport(CSVRecord row,
                               Map<String, String> countries) {
        String iata = row.get("iata_code");
        if (iata == null || iata.isBlank()) {
            return;
        }

        String type = row.get("type");
        if (!List.of("large_airport", "medium_airport", "small_airport")
                .contains(type)) {
            return;
        }

        Place place = new Place();
        place.setName(normalize(row.get("name")));
        place.setCity(normalize(row.get("municipality")));
        place.setCountry(
                countries.get(normalize(row.get("iso_country")))
        );
        place.setLatitude(
                Double.parseDouble(normalize(row.get("latitude_deg")))
        );
        place.setLongitude(
                Double.parseDouble(normalize(row.get("longitude_deg")))
        );
        placeRepository.persist(place);

        Airport airport = new Airport();
        airport.setPlace(place);
        airport.setIataCode(normalize(row.get("iata_code")));
        airport.setIcaoCode(normalize(row.get("icao_code")));
        airportRepository.persist(airport);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        value = value.trim();

        return (value.isEmpty() || "\\N".equals(value)) ? null : value;
    }


    @Transactional
    public void importAirlines() throws IOException {

        try (
                InputStream is = getClass()
                        .getResourceAsStream("/data/airlines.dat");
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader("id",
                                "name",
                                "alias",
                                "iata",
                                "icao",
                                "callsign",
                                "country",
                                "active")
                        .setSkipHeaderRecord(false)
                        .get()
                        .parse(reader)
        ) {
            for (CSVRecord row : parser) {
                importAirline(row);
            }
        }
    }

    private void importAirline(CSVRecord row) {
        String active = normalize(row.get("active"));

        if (!"Y".equals(active)) return;

        String name = normalize(row.get("name"));
        String iata = normalize(row.get("iata"));

        if (name == null || iata == null)
            return;

        if (iata.length() != 2) {
            return;
        }
        Optional<Airline> existing = airlineRepository.findAirlineByIataCode(iata);

        if (existing.isPresent()) {
            return;
        }

        Airline airline = new Airline();
        airline.setName(name);
        airline.setIataCode(iata);

        airlineRepository.persist(airline);

    }

}
