package travel.itinerary.controller;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import travel.itinerary.dto.AirlineDto;
import travel.itinerary.dto.CompanyDto;
import travel.itinerary.entity.carrier.CompanyType;
import travel.itinerary.service.CarrierService;

import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class CarrierController {
    @Inject
    CarrierService carrierService;

    @GET
    @Path("airlines")
    public Response getAirlines(){
        List<AirlineDto> airlineDtoList = carrierService.getAllAirlines();
        return Response.ok(airlineDtoList).build();
    }

    @GET
    @Path("companies")
    public Response getCompaniesByType(@QueryParam("type") CompanyType type){
        List<CompanyDto> companyDtoList = carrierService.getAllCompaniesByType(type);
        return Response.ok(companyDtoList).build();
    }
}
