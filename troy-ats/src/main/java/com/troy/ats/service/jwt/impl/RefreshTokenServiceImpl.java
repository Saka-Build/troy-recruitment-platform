package com.troy.ats.service.jwt.impl;

import com.troy.ats.service.jwt.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@Profile("redis")
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";
    private static final String USER_TOKENS_PREFIX = "refresh:user:";

    private final StringRedisTemplate redis;
    private final Duration refreshTtl;

    public RefreshTokenServiceImpl(
            StringRedisTemplate redis,
            @Value("${app.jwt.refresh-ttl-days:7}") long refreshTtlDays
    ) {
        this.redis = redis;
        this.refreshTtl = Duration.ofDays(refreshTtlDays);
    }

    /**
     * Redis:
     *
     * refresh:{tokenId} -> userId
     * TTL = refresh token lifetime
     *
     * refresh:user:{userId} -> Set of tokenIds
     */
    @Override
    public String issue(String userId) {

        String tokenId = UUID.randomUUID().toString();

        // Store token -> user mapping
        redis.opsForValue().set(refreshTokenKey(tokenId), userId, refreshTtl);
        // Store token ID against user
        redis.opsForSet().add(userTokensKey(userId), tokenId);
        // Keep user token set alive
        redis.expire(userTokensKey(userId), refreshTtl);

        return tokenId;
    }

    /**
     * Validate refresh token and consume it.
     *
     * A refresh token can only be used once.
     */
    @Override
    public String validateAndConsume(String tokenId) {

        if (tokenId == null || tokenId.isBlank()) {
            return null;
        }

        String userId = redis.opsForValue().get(
                refreshTokenKey(tokenId)
        );

        if (userId == null) {
            return null;
        }

        // Delete the refresh token so it cannot be reused
        redis.delete(refreshTokenKey(tokenId));
        // Remove token from user's token set
        redis.opsForSet().remove(userTokensKey(userId), tokenId);

        return userId;
    }

    /**
     * Revoke all refresh tokens belonging to a user.
     */
    @Override
    public void revokeAllForUser(String userId) {

        if (userId == null || userId.isBlank()) {
            return;
        }

        String userKey = userTokensKey(userId);

        Set<String> tokenIds = redis.opsForSet().members(userKey);

        if (tokenIds != null && !tokenIds.isEmpty()) {

            for (String tokenId : tokenIds) {
                redis.delete(refreshTokenKey(tokenId));
            }
        }

        // Delete user's token set
        redis.delete(userKey);
    }

    private String refreshTokenKey(String tokenId) {
        return REFRESH_TOKEN_PREFIX + tokenId;
    }

    private String userTokensKey(String userId) {
        return USER_TOKENS_PREFIX + userId;
    }
}