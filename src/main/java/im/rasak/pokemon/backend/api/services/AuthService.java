package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.dto.auth.LoginDto;
import im.rasak.pokemon.backend.api.dto.auth.RegisterDto;

public interface AuthService {

    void registerUser(RegisterDto registerDto);

    String loginUser(LoginDto loginDto);

    void logoutUser(String token);
}
