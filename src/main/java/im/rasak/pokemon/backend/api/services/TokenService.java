package im.rasak.pokemon.backend.api.services;

public interface TokenService {
    void blacklistToken(String token);

    boolean isTokenBlacklisted(String token);
}
