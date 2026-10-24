package com.troy.ats.repository;

import com.troy.ats.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenIdHash(String tokenIdHash);

    List<RefreshToken> findByUserIdAndRevokedFalse(String tokenIdHash);

    void deleteByTokenIdHash(String tokenIdHash);

    void deleteByUserId(String tokenIdHash);
}