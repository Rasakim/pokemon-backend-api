package im.rasak.pokemon.backend.api.exceptions;

import java.io.Serial;

public class EmailAlreadyTakenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1;

    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
