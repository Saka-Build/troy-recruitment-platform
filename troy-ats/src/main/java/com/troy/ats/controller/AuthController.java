package com.troy.ats.controller;

import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.SwitchRoleRequest;
import com.troy.ats.entity.*;
import com.troy.ats.exception.ServiceException;
import com.troy.ats.service.EmployeeService;
import com.troy.ats.service.SessionService;
import com.troy.ats.service.impl.RoleServiceImpl;
import com.troy.ats.service.jwt.JwtService;
import com.troy.ats.service.jwt.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_MINUTES = 15;
    private static final long ACCESS_TOKEN_TTL_SECONDS = 60 * 60;

    // One shared constant so "no such user" and "wrong password" are byte-identical
    // to the caller - anything else lets an attacker enumerate valid emails.
    private static final String INVALID_CREDENTIALS = "Invalid username or password";

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final SessionService sessionService;
    private final RoleServiceImpl roleService;

    public AuthController(EmployeeService employeeService, PasswordEncoder passwordEncoder,
                          JwtService jwtService, RefreshTokenService refreshTokenService, SessionService sessionService, RoleServiceImpl roleService) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.sessionService = sessionService;
        this.roleService = roleService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        Optional<Employee> employee = employeeService.getEmployeeByEmail(req.getEmailId());
        Employee user = employee.isPresent() ? employee.get() : null;

        if (user == null) {
            log.warn("Login failed - no account for email={}", req.getEmailId());
            throw new ServiceException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS);
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            log.warn("User is inactive for email={}", req.getEmailId());
            throw new ServiceException(HttpStatus.FORBIDDEN, "Account is inactive");
        }
        if (user.getLockedUntil() != null) {

            if (user.getLockedUntil().isAfter(Instant.now())) {
                log.warn("Login blocked - account {} locked until {}", user.getId(), user.getLockedUntil());
                throw new ServiceException(HttpStatus.LOCKED, "Account locked. Try again later.");
            }

            // Lockout window has passed
            user.setLockedUntil(null);
            user.setFailedLoginAttempts(0);
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setIsActive(true);
                user.setLockedUntil(Instant.now().plusSeconds(LOCKOUT_MINUTES * 60));
                log.warn("Account {} locked after {} failed attempts", user.getId(), user.getFailedLoginAttempts());
            }

            // Persist the attempt counter before unwinding - the advice turns this
            // throw into the response, it does not roll anything back.
            employeeService.updateEmployee(user.getId(), user);
            log.warn("Login failed - bad password for account {} (attempt {})", user.getId(), user.getFailedLoginAttempts());
            throw new ServiceException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS);
        }

        // success -> reset failure counter
        user.setFailedLoginAttempts(0);
        employeeService.updateEmployee(user.getId(), user);

        List<UserRole> roles = roleService.findActiveRolesWithPermissions(user.getId());

        if (roles.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "No role assigned to account");
        }
        UserRole selectedRole = roles.get(0);
        String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getOfficialEmail(), selectedRole.getRole().getId(), selectedRole.getRole().getName().name());
        String refreshToken = refreshTokenService.issue(user.getId().toString());

        log.info("Login success for account {}", user.getId());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        tokenResponse.setExpiresInSeconds(ACCESS_TOKEN_TTL_SECONDS);

        tokenResponse = roleService.getActiveRolesForToken(tokenResponse, user.getId(), selectedRole.getRole().getId());

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        String userId = refreshTokenService.validateAndConsume(req.getRefreshToken());
        if (userId == null) {
            // Token unknown/expired/already-used. Could be legitimate expiry,
            // or reuse of a stolen+already-rotated token - treat cautiously.
            log.warn("Refresh rejected - unknown, expired or already-consumed token");
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }

        Employee user = employeeService.getEmployeeById(UUID.fromString(userId));
        if (user == null || ! user.getIsActive()) {
            log.warn("Refresh rejected - account {} missing or inactive", userId);
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Account unavailable");
        }

        List<UserRole> roles = roleService.findActiveRolesWithPermissions(user.getId());

        if (roles.isEmpty()) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "No role assigned to account");
        }
        UserRole selectedRole = roles.get(0);
        String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getOfficialEmail(), selectedRole.getRole().getId(), selectedRole.getRole().getName().name());
        String newRefreshToken = refreshTokenService.issue(user.getId().toString()); // rotation

        log.info("Refresh success for account {}", user.getId());
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(newRefreshToken);
        tokenResponse.setExpiresInSeconds(ACCESS_TOKEN_TTL_SECONDS);

        tokenResponse = roleService.getActiveRolesForToken(tokenResponse, user.getId(), selectedRole.getRole().getId());

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeDto> login(final HttpServletRequest req, HttpServletResponse res) {

        // getCurrentUser() now throws 401 rather than returning null, so there is
        // no "no user" branch left to handle here.
        Employee user = sessionService.getCurrentUser();
        return ResponseEntity.ok(employeeService.getEmployeeDtoFromEntity(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest req) {
        String userId = refreshTokenService.validateAndConsume(req.getRefreshToken());
        if (userId != null) {
            refreshTokenService.revokeAllForUser(userId); // kill every session, not just this one
            log.info("Logout - revoked all sessions for account {}", userId);
        }
        // 204 either way: a stale token being "logged out" is not an error.
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/switchRole")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TokenResponse> switchRole(
            @Valid @RequestBody SwitchRoleRequest request) {

        // Get currently authenticated employee
        Employee user = sessionService.getCurrentUser();

        if (user == null) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        // Verify that this role belongs to the logged-in employee
        // and that the assignment is active.
        UserRole selectedRole = roleService.validateEmployeeRole(user.getId(), request.getRoleId());

        Role role = selectedRole.getRole();

        // Generate a NEW access token containing the selected role
        String accessToken = jwtService.generateAccessToken(
                user.getId().toString(),
                user.getOfficialEmail(),
                role.getId(),
                role.getName().name()
        );


        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setExpiresInSeconds(ACCESS_TOKEN_TTL_SECONDS);

        tokenResponse = roleService.getActiveRolesForToken(tokenResponse, user.getId(), selectedRole.getRole().getId());


        log.info(
                "Role switched successfully. userId={}, roleId={}, role={}", user.getId(), role.getId(), role.getName());

        return ResponseEntity.ok(tokenResponse);
    }
}