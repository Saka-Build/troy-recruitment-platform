package com.troy.ats.service.jwt;

/**
 * Contract for refresh token storage/rotation. Two implementations:
 *  - RedisRefreshTokenService    (prod / any multi-instance environment)
 *  - InMemoryRefreshTokenService (local dev / tests - no Redis dependency)
 */
public interface RefreshTokenService {

    /** Issues a new refresh token for the user and returns its opaque id. */
    String issue(String userId);

    /** Returns the userId if valid, consuming (rotating out) the token. Null if invalid/expired/reused. */
    String validateAndConsume(String tokenId);

    /** Revokes every active refresh token for the user (logout, theft detection). */
    void revokeAllForUser(String userId);
}
