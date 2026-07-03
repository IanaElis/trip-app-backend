package travel.util;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import travel.itinerary.repository.AirlineRepository;
import travel.map.repository.AirportRepository;

@ApplicationScoped
public class DatabaseSeeder {
    @Inject
    ImportService importService;

    @Inject
    AirportRepository airportRepository;
    @Inject
    AirlineRepository airlineRepository;

    void onStart(@Observes StartupEvent event) throws Exception {
        if (airportRepository.count() == 0) {
            importService.importAirports();
            System.out.println("Imported airports.");
        }
        if(airlineRepository.count() == 0) {
            importService.importAirlines();
            System.out.println("Imported airlines.");
        }
    }
}
