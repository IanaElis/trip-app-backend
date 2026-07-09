package user.controller;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import user.dto.*;
import user.dto.request.*;
import user.service.AuthenticationService;
import user.service.CookieService;

import java.time.Duration;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class UserController {

    @Inject
    AuthenticationService authService;
    @Inject
    CookieService cookieService;
    @Inject
    JsonWebToken jwt;
    @Inject
    SecurityIdentity securityIdentity;

    private static final Duration ACCESS_TTL = Duration.ofMinutes(15);
    private static final Duration REFRESH_TTL = Duration.ofDays(14);
    private static final String ACCESS_TOKEN_NAME = "access_token";
    private static final String REFRESH_TOKEN_NAME = "refresh_token";


    @POST
    @Path("/login")
    public Response login(@Valid LoginDto authDto){
        TokenPair tokens = authService.login(authDto);
        NewCookie cookie = cookieService
                .createCookie(ACCESS_TOKEN_NAME, tokens.accessToken(), (int)ACCESS_TTL.toSeconds());
        NewCookie cookie2 = cookieService
                .createCookie(REFRESH_TOKEN_NAME, tokens.refreshToken(), (int)REFRESH_TTL.toSeconds());
        return Response.ok().cookie(cookie, cookie2).build(); // or no content?
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterDto dto) {
        TokenPair tokens = authService.register(dto);
        NewCookie cookie = cookieService
                .createCookie(ACCESS_TOKEN_NAME, tokens.accessToken(), (int)ACCESS_TTL.toSeconds());
        NewCookie cookie2 = cookieService
                .createCookie(REFRESH_TOKEN_NAME, tokens.refreshToken(), (int)REFRESH_TTL.toSeconds());

        return Response.ok().cookie(cookie, cookie2).build();
    }

    @POST
    @Path("/refresh")
    public Response refresh(@CookieParam("refresh_token") String refreshToken) {
        if(refreshToken == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        TokenPair tokens = authService.refresh(refreshToken);
        NewCookie cookie = cookieService
                .createCookie(ACCESS_TOKEN_NAME, tokens.accessToken(), (int)ACCESS_TTL.toSeconds());
        NewCookie cookie2 = cookieService
                .createCookie(REFRESH_TOKEN_NAME, tokens.refreshToken(), (int)REFRESH_TTL.toSeconds());

        return Response.ok().cookie(cookie, cookie2).build();
    }

    @POST
    @Path("/logout")
    @Authenticated
    public Response logout(@CookieParam("refresh_token") String refreshToken) {
        if(refreshToken == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        authService.logout(refreshToken);
        NewCookie cookie = cookieService.clearCookie(ACCESS_TOKEN_NAME);
        NewCookie cookie2 = cookieService.clearCookie(REFRESH_TOKEN_NAME);
        return Response.ok().cookie(cookie, cookie2).build();
    }

    @POST
    @Path("/forgot-password")
    public Response forgotPassword(@Valid ForgotPasswordDto dto) {
        authService.forgotPassword(dto);
        return Response.ok().build();
    }

    @POST
    @Path("/reset-password")
    public Response resetPassword(@Valid ResetPasswordDto dto) {
        authService.resetPassword(dto);
        return Response.ok().build();
    }

    @GET
    @Path("/me")
    @Authenticated
    public Response me() {
        String userId = jwt.getSubject();

        if (userId == null) {
            return Response.status(401).build();
        }
        UserDto userInfo = authService.currentUser(Long.parseLong(userId));
        return Response.ok(userInfo).build();
    }

    @PUT
    @Path("/profile")
    public Response updateProfile(@Valid UpdateProfileDto dto) {
        String userId = jwt.getSubject();

        if (userId == null) {
            return Response.status(401).build();
        }
        UserDto userInfo = authService.updateProfile(Long.parseLong(userId), dto);
        return Response.ok(userInfo).build();
    }


}
