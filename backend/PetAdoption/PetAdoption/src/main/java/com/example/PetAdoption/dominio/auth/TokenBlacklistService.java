package com.example.PetAdoption.dominio.auth;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    public void revoke(String token, Instant expiration) {
        blacklistedTokens.put(token, expiration);
    }

    public boolean isRevoked(String token) {
        Instant exp = blacklistedTokens.get(token);
        return exp != null && Instant.now().isBefore(exp);
    }

    @Scheduled(fixedRate = 3600000) // 1 hour
    public void cleanUpExpiredTokens() {
        Instant now = Instant.now();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now.minusSeconds(3600)));
    }
}
