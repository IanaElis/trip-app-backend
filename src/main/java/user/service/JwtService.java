package user.service;

import io.smallrye.jwt.auth.principal.ParseException;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import user.entity.User;

public interface JwtService {
    String jwtGenerator(User user);
}
