package travel.itinerary.controller.user;

import io.quarkus.security.Authenticated;
import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import travel.itinerary.dto.request.CreateTripRequest;
import travel.itinerary.dto.response.ShortTripDto;
import travel.itinerary.dto.timeline.FullItineraryDto;
import travel.itinerary.dto.timeline.TripDto;
import travel.itinerary.service.ItineraryService;
import travel.itinerary.service.TripService;

import java.net.URI;
import java.util.List;

@Path("/trips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class TripController {
    @Inject
    TripService tripService;
    @Inject
    ItineraryService itineraryService;
    private static final Long userId = 1L;
    @Inject
    JsonWebToken jwt;

    Long getUserId() {
        String subject = jwt.getSubject();
        return Long.parseLong(subject);
    }

    @POST
    @Path("/")
    public Response createTrip(CreateTripRequest createTripRequest) {
        ShortTripDto tripCreated = tripService.createTrip(getUserId(), createTripRequest);
        return Response.seeOther(URI
               .create("/trips/" + tripCreated.id())).build();
        //return Response.status(Response.Status.CREATED).entity(tripCreated).build();
    }

    @PUT
    @Path("/{trip_id}")
    public Response updateTrip(@PathParam("trip_id") Long tripId, CreateTripRequest dto){
        TripDto tripUpdated = tripService.updateTrip(getUserId(), tripId, dto);
        return Response.ok(tripUpdated).build();
    }

//    @GET
//    @Path("/{trip_id}")
//    public Response getTrip(@PathParam("trip_id") Long tripId) {
//        TripDto result = tripService.getTripById(userId, tripId);
//        return Response.ok(result).build();
//    }

    @GET
    @Path("/{trip_id}")
    public Response getTrip(@PathParam("trip_id") Long tripId) {
        FullItineraryDto result = null;
        try {
            result = itineraryService.getItinerary(getUserId(), tripId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return Response.ok(result).build();
    }

    @GET
    public Response getAllTripsForUser() {
        List<ShortTripDto> result = tripService.getAllTrips(getUserId());
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/{trip_id}")
    public Response deleteTrip(@PathParam("trip_id") Long tripId) {
        tripService.deleteTrip(userId, tripId);
        return Response.seeOther(URI.create("/trips")).build(); //?
    }

}
