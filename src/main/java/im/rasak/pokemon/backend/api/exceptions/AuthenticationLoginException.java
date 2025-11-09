package im.rasak.pokemon.backend.api.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

@Getter
@Setter
public class AuthenticationLoginException extends AuthenticationException {

    Map<String, String> errors;

    public AuthenticationLoginException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
