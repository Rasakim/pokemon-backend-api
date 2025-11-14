package im.rasak.pokemon.backend.api.responses;

import im.rasak.pokemon.backend.api.exceptions.ErrorObject;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private List<ErrorObject> errors;
    private long timestamp;

    public ApiResponse() {
        this.timestamp = Instant.now().toEpochMilli();
    }

    public ApiResponse(boolean success, T data, String message, List<ErrorObject> errors) {
        this();
        this.success = success;
        this.data = data;
        this.message = message;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> fail(String message, List<ErrorObject> errors) {
        return new ApiResponse<>(false, null, message, errors);
    }
}
