package travel.itinerary.controller.user;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
//@Authenticated
public class TripController {
    @Inject
    TripService tripService;
    @Inject
    ItineraryService itineraryService;
    private static final Long userId = 1L;

    @POST
    public Response createTrip(CreateTripRequest createTripRequest) {
        ShortTripDto tripCreated = tripService.createTrip(createTripRequest);
        return Response.seeOther(URI
                .create("/trip/" + tripCreated.id())).build();
    }

    @PUT
    @Path("/{trip_id}")
    public Response updateTrip(@PathParam("trip_id") Long tripId, CreateTripRequest dto){
        TripDto tripUpdated = tripService.updateTrip(userId, tripId, dto);
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
        FullItineraryDto result = itineraryService.getItinerary(userId, tripId);
        return Response.ok(result).build();
    }

    @GET
    public Response getAllTripsForUser() {
        List<ShortTripDto> result = tripService.getAllTrips(userId);
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/{trip_id}")
    public Response deleteTrip(@PathParam("trip_id") Long tripId) {
        tripService.deleteTrip(userId, tripId);
        return Response.seeOther(URI.create("/trips")).build(); //?
    }

}
