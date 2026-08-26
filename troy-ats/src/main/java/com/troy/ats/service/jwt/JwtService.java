package com.troy.ats.service.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface JwtService {

    /** Issues a signed, short-lived access token for the given user. */
    String generateAccessToken(String userId, String username, UUID roleId, String roleName);

    /** Throws JwtException (expired, malformed, bad signature) if invalid. */
    Claims parseAndValidate(String token);
    UUID getUserId(Claims claims);
    UUID getRoleId(Claims claims);
    String getRoleName(Claims claims);
}