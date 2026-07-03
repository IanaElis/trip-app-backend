package user.controller;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import user.dto.*;
import user.service.AuthenticationService;
import user.service.CookieService;
import user.service.JwtService;

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

    private static final Duration ACCESS_TTL = Duration.ofMinutes(15);
    private static final Duration REFRESH_TTL = Duration.ofDays(14);
    private static final String ACCESS_TOKEN_NAME = "access_token";
    private static final String REFRESH_TOKEN_NAME = "refresh_toekn";


    @POST
    @Path("/login")
    public Response login(@Valid LoginDto authDto) throws ParseException {
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

        TokenPair tokens = authService.refresh(refreshToken);
        NewCookie cookie = cookieService
                .createCookie(ACCESS_TOKEN_NAME, tokens.accessToken(), (int)ACCESS_TTL.toSeconds());
        NewCookie cookie2 = cookieService
                .createCookie(REFRESH_TOKEN_NAME, tokens.refreshToken(), (int)REFRESH_TTL.toSeconds());


        return Response.ok().cookie(cookie, cookie2).build();
    }

    @POST
    @Path("/logout")
    public Response logout(@CookieParam("refresh_token") String refreshToken) {
        authService.logout(refreshToken);
        NewCookie cookie = cookieService.clearCookie(REFRESH_TOKEN_NAME);
        return Response.ok().cookie(cookie).build();
    }

    @POST
    @Path("/forgot-password")
    public Response forgotPassword(ForgotPasswordDto dto) {
        authService.forgotPassword(dto);
        return Response.ok().build();
    }

    @POST
    @Path("/reset-password")
    public Response resetPassword(ResetPasswordDto dto) {
        boolean success = authService.resetPassword(dto);
        if(success) {
            return Response.ok().build();
        }
        else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/me")
    public Response me() {
        UserDto userInfo = authService.currentUser(Long.parseLong(jwt.getSubject()));
        return Response.ok(userInfo).build();
    }


}
