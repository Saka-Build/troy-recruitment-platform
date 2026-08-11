package com.troy.ats.controller;

import com.troy.ats.entity.Employee;
import com.troy.ats.entity.LoginRequest;
import com.troy.ats.entity.RefreshTokenRequest;
import com.troy.ats.entity.TokenResponse;
import com.troy.ats.service.jwt.EmployeeService;
import com.troy.ats.service.jwt.JwtService;
import com.troy.ats.service.jwt.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_MINUTES = 15;
    private static final long ACCESS_TOKEN_TTL_SECONDS = 10 * 60;

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(EmployeeService employeeService, PasswordEncoder passwordEncoder,
                          JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Employee user = employeeService.findByEmailId(req.getEmailId());

        // Deliberately identical error for "no such user" and "wrong password" -
        // don't leak which one it was (prevents username enumeration).
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        if (user.getIsActive()) {
            if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("Account locked. Try again later.");
            }
            // lockout window passed -> reset
            user.setIsActive(false);
            user.setFailedLoginAttempts(0);
        }

        if (!passwordEncoder.matches(passwordEncoder.encode(req.getPassword()), user.getPasswordHash())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setIsActive(true);
                user.setLockedUntil(Instant.now().plusSeconds(LOCKOUT_MINUTES * 60));
            }

            employeeService.updateEmployee(user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // success -> reset failure counter
        user.setFailedLoginAttempts(0);
        employeeService.updateEmployee(user);

        String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getOfficialEmail(), user.getRole().name());
        String refreshToken = refreshTokenService.issue(user.getId().toString());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken, ACCESS_TOKEN_TTL_SECONDS));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        String userId = refreshTokenService.validateAndConsume(req.getRefreshToken());
        if (userId == null) {
            // Token unknown/expired/already-used. Could be legitimate expiry,
            // or reuse of a stolen+already-rotated token - treat cautiously.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        Employee user = employeeService.findById(UUID.fromString(userId));
        if (user == null || ! user.getIsActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account unavailable");
        }

        String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getOfficialEmail(), user.getRole().name());
        String newRefreshToken = refreshTokenService.issue(user.getId().toString()); // rotation
        //return null;
        return ResponseEntity.ok(new TokenResponse(accessToken, newRefreshToken, ACCESS_TOKEN_TTL_SECONDS));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest req) {
        String userId = refreshTokenService.validateAndConsume(req.getRefreshToken());
        if (userId != null) {
            refreshTokenService.revokeAllForUser(userId); // kill every session, not just this one
        }
        return ResponseEntity.noContent().build();
    }
}
