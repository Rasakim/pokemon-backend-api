package im.rasak.pokemon.backend.api.exceptions;

public class AuthenticationBlacklistedTokenException extends RuntimeException {
    public AuthenticationBlacklistedTokenException(String message) {
        super(message);
    }
}
