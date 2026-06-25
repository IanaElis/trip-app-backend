package user.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

@ApplicationScoped
public class JwtService {
    @ConfigProperty(name = "mp.jwt.private-key.location")
    String privateKeyLocation;

    @ConfigProperty(name ="mp.jwt.verify.issuer")
    String verifyIssuer;

    public String jwtGenerator(String username, user.entity.Role role, Long userId) {
        return Jwt.issuer(verifyIssuer)
                .subject(username)
                .claim("userid", userId)
                .groups(role.toString())
                .expiresIn(Duration.ofDays(7))
                .sign(privateKeyLocation);
    }
}
