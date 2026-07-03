package user.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import user.service.AdminService;

@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class AdminController {
    @Inject
    AdminService adminService;

    @POST
    @Path("/users/{id}/block")
    public Response blockUser(@PathParam("id") Long id) {
        adminService.blockUser(id);
        return Response.ok().build();
    }

    @DELETE
    @Path("/users/{id}/block")
    public Response unblockUser(@PathParam("id") Long id) {
        adminService.unblockUser(id);
        return Response.ok().build();
    }

    @GET
    @Path("/blocked-users")
    public Response getBlockedUsers() {
        return Response.ok().entity(adminService.getBlockedUsers()).build();
    }

    @GET
    @Path("/users/{id}")
    public Response getUser(@PathParam("id") Long id) {
        return Response.ok().entity(adminService.getUser(id)).build();
    }

    @GET
    @Path("/users")
    public Response getUsers() {
        return Response.ok(adminService.getAllUsers()).build();
    }

}
