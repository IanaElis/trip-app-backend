package user.service.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.NewCookie;
import user.service.CookieService;

import java.time.Duration;

@ApplicationScoped
public class CookieServiceImpl implements CookieService {

    @Override
    public NewCookie createCookie(String name, String value, int maxAge) {
        return new NewCookie.Builder(name)
                .value(value)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(maxAge)
                .sameSite(NewCookie.SameSite.LAX)
                .build();
    }

    @Override
    public NewCookie clearCookie(String name) {
        return new NewCookie.Builder(name)
                .value("")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .sameSite(NewCookie.SameSite.LAX)
                .build();
    }
}
