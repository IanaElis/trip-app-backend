package user.service.implementation;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import user.entity.User;
import user.service.JwtService;

import java.time.Duration;

@ApplicationScoped
public class JwtServiceImpl implements JwtService {
    @ConfigProperty(name = "mp.jwt.private-key.location")
    String privateKeyLocation;

    @ConfigProperty(name ="mp.jwt.verify.issuer")
    String verifyIssuer;


    public String jwtGenerator(User user) {
        return Jwt.issuer(verifyIssuer)
                .subject(user.getId().toString())
                .groups(user.getRole().toString())
                .expiresIn(Duration.ofMinutes(15))
                .claim(Claims.email.name(), user.getEmail())
                .sign(privateKeyLocation);
    }
}
