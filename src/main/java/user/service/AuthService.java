package user.service;

import user.dto.AuthDto;

public interface AuthService {
    String login(AuthDto dto);
    String register(AuthDto dto);
}
