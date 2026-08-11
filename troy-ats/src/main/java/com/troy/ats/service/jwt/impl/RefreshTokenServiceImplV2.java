package com.troy.ats.service.jwt.impl;

import com.troy.ats.entity.RefreshToken;
import com.troy.ats.repository.RefreshTokenRepository;
import com.troy.ats.service.jwt.RefreshTokenService;
import com.troy.ats.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Profile("database")
public class RefreshTokenServiceImplV2 implements RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final Duration refreshTtl;

    public RefreshTokenServiceImplV2(
            RefreshTokenRepository repository,
            @Value("${app.jwt.refresh-ttl-days:7}") long refreshTtlDays
    ) {
        this.repository = repository;
        this.refreshTtl = Duration.ofDays(refreshTtlDays);
    }

    /**
     * Issue Token
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public String issue(String userId) {

        String tokenId = UUID.randomUUID().toString();
        String tokenHash = HashUtil.sha256(tokenId);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setTokenId(tokenHash);
        refreshToken.setUserId(userId);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(refreshTtl));
        refreshToken.setRevoked(false);

        repository.save(refreshToken);

        return tokenId;
    }

    /**
     * Validate and consume token
     * @param tokenId
     * @return
     */
    @Override
    @Transactional
    public String validateAndConsume(String tokenId) {

        if (tokenId == null || tokenId.isBlank()) {
            return null;
        }
        String tokenHash = HashUtil.sha256(tokenId);
        RefreshToken refreshToken = repository.findByTokenIdHash(tokenHash).orElse(null);

        if (refreshToken == null) {
            return null;
        }

        // Already revoked
        if (refreshToken.isRevoked()) {
            return null;
        }

        // Expired
        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            repository.save(refreshToken);
            return null;
        }

        String userId = refreshToken.getUserId();

        // One-time-use refresh token
        refreshToken.setRevoked(true);
        repository.save(refreshToken);

        return userId;
    }

    /**
     * delete token
     * @param userId
     */
    @Override
    @Transactional
    public void revokeAllForUser(String userId) {

        var tokens = repository.findByUserIdAndRevokedFalse(userId);

        for (RefreshToken token : tokens) {
            token.setRevoked(true);
        }

        repository.saveAll(tokens);
    }
}