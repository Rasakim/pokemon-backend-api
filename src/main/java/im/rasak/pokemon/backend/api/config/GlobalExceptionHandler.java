package im.rasak.pokemon.backend.api.config;

import im.rasak.pokemon.backend.api.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<ErrorObject> handlePokemonNotFoundException(PokemonNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorObject> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, errors);
    }


    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<ErrorObject> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ErrorObject> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(AuthenticationLoginException.class)
    public ResponseEntity<ErrorObject> AuthenticationException(AuthenticationLoginException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, ex.getErrors());
    }

    @ExceptionHandler(AuthenticationBlacklistedTokenException.class)
    public ResponseEntity<ErrorObject> BlacklistedTokenException(AuthenticationBlacklistedTokenException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, null);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorObject> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, null);
    }

    private ResponseEntity<ErrorObject> buildErrorResponse(String message, HttpStatus status, Map<String, String> errors) {
        HttpServletRequest request = getCurrentHttpRequest();
        String clientIp = request != null ? request.getRemoteAddr() : "unknown";
        String url = request != null ? request.getRequestURI() : "unknown";
        String method = request != null ? request.getMethod() : "unknown";
        String userAgent = request != null ? request.getHeader("User-Agent") : "unknown";
        String sessionId = (request != null && request.getSession(false) != null) ? request.getSession(false).getId() : "none";

        String authUserName = "anonymous";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            authUserName = authentication.getName();
        }

        String errorDetails = "";
        if (errors != null && !errors.isEmpty()) {
            errorDetails = errors.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining(", "));
        }

        if (status.is5xxServerError()) {
            log.error("Request failed: HTTP {} - {}, Errors: [{}], User: {}, IP: {}, URL: {}, Method: {}, User-Agent: {}, Session-ID: {}",
                    status.value(), message, errorDetails, authUserName, clientIp, url, method, userAgent, sessionId);
        } else if (status.is4xxClientError()) {
            log.warn("Client error: HTTP {} - {}, Errors: [{}], User: {}, IP: {}, URL: {}, Method: {}, User-Agent: {}, Session-ID: {}",
                    status.value(), message, errorDetails, authUserName, clientIp, url, method, userAgent, sessionId);
        } else {
            log.info("HTTP {} - {}, Errors: [{}], User: {}, IP: {}, URL: {}, Method: {}, User-Agent: {}, Session-ID: {}",
                    status.value(), message, errorDetails, authUserName, clientIp, url, method, userAgent, sessionId);
        }

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(status.value());
        errorObject.setMessage(message);
        errorObject.setTimestamp(new Date());
        if (errors != null) {
            errorObject.setErrors(errors);
        }

        return new ResponseEntity<>(errorObject, status);
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest();
    }
}
