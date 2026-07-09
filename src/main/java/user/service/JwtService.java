package user.service;

import user.entity.User;

public interface JwtService {
    String jwtGenerator(User user);
}
