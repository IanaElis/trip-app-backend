package user.service;

import jakarta.ws.rs.core.NewCookie;

public interface CookieService {
   // NewCookie createAuthenticationCookie(String jwt);
  //  NewCookie clearCookie();

    NewCookie createCookie(String name, String value, int maxAge);

    NewCookie clearCookie(String name);
}
