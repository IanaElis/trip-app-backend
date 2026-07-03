package travel.itinerary.controller.user;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import travel.itinerary.dto.request.CreateAccommodationRequest;
import travel.itinerary.dto.request.CreateActivityRequest;
import travel.itinerary.dto.request.CreateFlightRequest;
import travel.itinerary.dto.request.CreateTransportRequest;
import travel.itinerary.dto.response.FullAccommodationDto;
import travel.itinerary.dto.response.FullActivityDto;
import travel.itinerary.dto.response.FullFlightDto;
import travel.itinerary.dto.response.FullTransportDto;
import travel.itinerary.dto.timeline.FullItineraryDto;
import travel.itinerary.dto.timeline.TimelineItemDto;
import travel.itinerary.service.ItineraryService;

import java.net.URI;

@Path("/trips/{trip_id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@Authenticated
public class ItineraryController {
    @Inject
    ItineraryService itineraryService;
    private static final Long userId = 1L;

    @POST
    @Path("/accommodation/create")
    public Response createAccommodation(@PathParam("trip_id") Long tripId,
            CreateAccommodationRequest dto){
        TimelineItemDto created = null;
        try{
        created = itineraryService
                .addAccommodation(userId, tripId, dto);}
        catch(Exception e){
            e.printStackTrace();
        }
        return Response.ok(created).build();
                //Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @POST
    @Path("/activity/create")
    public Response createActivity(@PathParam("trip_id") Long tripId,
                                        CreateActivityRequest dto){
        TimelineItemDto created = itineraryService
                .addActivity(userId, tripId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @POST
    @Path("/transport/create")
    public Response createTransport(@PathParam("trip_id") Long tripId,
                                        CreateTransportRequest dto){
        TimelineItemDto created = itineraryService
                .addTransport(userId, tripId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @POST
    @Path("/flight/create")
    public Response createFlight(@PathParam("trip_id") Long tripId,
                                        CreateFlightRequest dto){
        TimelineItemDto created = itineraryService
                .addFlight(userId, tripId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @PUT
    @Path("/accommodation/{id}")
    public Response updateAccommodation(@PathParam("trip_id") Long tripId,
                                        @PathParam("id") Long itemId,
                                        CreateAccommodationRequest dto){
        TimelineItemDto updated = itineraryService.updateAccommodation(itemId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @PUT
    @Path("/activity/{id}")
    public Response updateActivity(@PathParam("trip_id") Long tripId,
                                        @PathParam("id") Long itemId,
                                        CreateActivityRequest dto){
        TimelineItemDto updated = itineraryService.updateActivity(itemId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @PUT
    @Path("/transport/{id}")
    public Response updateTransport(@PathParam("trip_id") Long tripId,
                                        @PathParam("id") Long itemId,
                                        CreateTransportRequest dto){
        TimelineItemDto updated = itineraryService.updateTransport(itemId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @PUT
    @Path("/flight/{id}")
    public Response updateFlight(@PathParam("trip_id") Long tripId,
                                        @PathParam("id") Long itemId,
                                        CreateFlightRequest dto){
        TimelineItemDto updated = itineraryService.updateFlight(itemId, dto);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItineraryItem(@PathParam("trip_id") Long tripId,
                                        @PathParam("id") Long itemId) {
        itineraryService.deleteItem(itemId);
        return Response.seeOther(URI.create("/trips/" + tripId)).build();
    }

    @GET
    @Path("/accommodation/{id}")
    public Response getAccommodation(@PathParam("id") Long itemId){
        FullAccommodationDto result = itineraryService.getAccommodationById(itemId);
        return Response.ok(result).build();
    }

    @GET
    @Path("/activity/{id}")
    public Response getActivity(@PathParam("id") Long itemId){
        FullActivityDto result = itineraryService.getActivityById(itemId);
        return Response.ok(result).build();
    }
    @GET
    @Path("/transport/{id}")
    public Response getTransport(@PathParam("id") Long itemId){
        FullTransportDto result = itineraryService.getTransportById(itemId);
        return Response.ok(result).build();
    }
    @GET
    @Path("/flight/{id}")
    public Response getFlight(@PathParam("id") Long itemId){
        FullFlightDto result = itineraryService.getFlightById(itemId);
        return Response.ok(result).build();
    }

    @GET
    @Path("/itinerary")
    public Response getItinerary(@PathParam("trip_id") Long tripId){
        FullItineraryDto result = itineraryService.getItinerary(userId, tripId);
        return Response.ok(result).build();
    }

}
