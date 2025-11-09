package im.rasak.pokemon.backend.api.services.impl;

import im.rasak.pokemon.backend.api.security.JwtUtil;
import im.rasak.pokemon.backend.api.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;
    private final ConcurrentHashMap<String, Date> blacklistedTokens = new ConcurrentHashMap<>();

    @Override
    public void blacklistToken(String token) {
        String username = jwtUtil.getUsernameFromJWT(token);
        Date expireDate = jwtUtil.getExpireDateFromJWT(token);
        blacklistedTokens.put(token, expireDate);
        log.info("Token for User {} blacklisted until: {}", username, expireDate);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {

        Date currentDate = new Date();
        Date expiryDate = blacklistedTokens.get(token);

        if (expiryDate == null) {
            return false;
        }

        if (currentDate.after(expiryDate)) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }

    @Scheduled(fixedRate = 3_600_000)
    public void cleanUpExpiredTokens() {
        Date currentDate = new Date();
        AtomicInteger numDeletedTokens = new AtomicInteger(0);
        blacklistedTokens.entrySet().removeIf(entry -> {
            boolean remove = currentDate.after(entry.getValue());
            if (remove) numDeletedTokens.incrementAndGet();
            return remove;
        });
        log.info("Scheduled clean up for expired blacklisted tokens, deleted {} tokens", numDeletedTokens.get());
    }
}
