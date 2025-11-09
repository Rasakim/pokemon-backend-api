package im.rasak.pokemon.backend.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObject {

    private Integer statusCode;
    private String message;
    private Map<String, String> errors;
    private Date timestamp;
}
