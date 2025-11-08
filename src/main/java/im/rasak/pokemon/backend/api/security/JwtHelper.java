package im.rasak.pokemon.backend.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-time}")
    private long jwtExpirationTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationTime);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token) throws AuthenticationException {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired at: {}", ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Token expired", ex);
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT: {}", ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT", ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT format: {}", ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Invalid JWT", ex);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Invalid signature", ex);
        } catch (Exception ex) {
            log.error("JWT validation failed: {}", ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT validation failed", ex);
        }
    }
}
