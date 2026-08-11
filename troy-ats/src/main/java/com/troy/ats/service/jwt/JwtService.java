package com.troy.ats.service.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    /** Issues a signed, short-lived access token for the given user. */
    String generateAccessToken(String userId, String username, String role);

    /** Throws JwtException (expired, malformed, bad signature) if invalid. */
    Claims parseAndValidate(String token);
}
