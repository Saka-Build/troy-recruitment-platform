package com.troy.ats.service.jwt.impl;

import com.troy.ats.service.jwt.RefreshTokenService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Drop-in replacement for RedisRefreshTokenService when running tests or
 * local dev without a Redis instance. Same contract, no TTL enforcement
 * (fine for short-lived test runs) and state is lost on restart - never use in prod.
 */
@Service
@Profile("test")
public class InMemoryRefreshTokenServiceImpl implements RefreshTokenService {

    private final Map<String, String> tokenToUser = new ConcurrentHashMap<>();

    @Override
    public String issue(String userId) {
        String tokenId = UUID.randomUUID().toString();
        tokenToUser.put(tokenId, userId);
        return tokenId;
    }

    @Override
    public String validateAndConsume(String tokenId) {
        return tokenToUser.remove(tokenId); // returns null if absent, same contract as Redis impl
    }

    @Override
    public void revokeAllForUser(String userId) {
        tokenToUser.entrySet().removeIf(e -> e.getValue().equals(userId));
    }
}
