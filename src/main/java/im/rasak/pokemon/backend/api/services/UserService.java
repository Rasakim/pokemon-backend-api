package im.rasak.pokemon.backend.api.services;

import im.rasak.pokemon.backend.api.models.UserEntity;

public interface UserService {

    UserEntity getUserEntityFromToken(String jwtToken);
}
