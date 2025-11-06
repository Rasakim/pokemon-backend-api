package im.rasak.pokemon.backend.api.exceptions;

import java.io.Serial;

public class UsernameAlreadyTakenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1;

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
