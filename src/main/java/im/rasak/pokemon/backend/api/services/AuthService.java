package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.dto.LoginDto;
import im.rasak.pokemon.backend.api.dto.RegisterDto;

public interface AuthService {

    void registerUser(RegisterDto registerDto);

    String loginUser(LoginDto loginDto);
}
