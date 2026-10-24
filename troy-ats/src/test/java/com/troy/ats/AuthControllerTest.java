/*
package com.troy.ats.controller;

import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.LoginRequest;
import com.troy.ats.entity.RefreshTokenRequest;
import com.troy.ats.entity.TokenResponse;
import com.troy.ats.enums.UserRole;
import com.troy.ats.exception.ServiceException;
import com.troy.ats.service.EmployeeService;
import com.troy.ats.service.SessionService;
import com.troy.ats.service.jwt.JwtService;
import com.troy.ats.service.jwt.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserRole userRole;

    */
/*
     * Deep stubs are used because AuthController calls:
     *
     * employee.getRole().name()
     *
     * This avoids needing to know the exact Role enum type here.
     *//*

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Employee employee;

    @Mock
    private EmployeeDto employeeDto;

    @InjectMocks
    private AuthController authController;

    private UUID employeeId;

    @BeforeEach
    void setUp() {

        employeeId = UUID.randomUUID();

        when(employee.getId())
                .thenReturn(employeeId);

        */
/*when(employee.getOfficialEmail())
                .thenReturn("employee@test.com");*//*


        */
/*when(employee.getPasswordHash())
                .thenReturn("hashedPassword");*//*


        */
/*
         * AuthController calls:
         *
         * user.getRole().name()
         *//*

       */
/* when(employee.getRole().name())
                .thenReturn("ADMIN");*//*

    }

    // ============================================================
    // LOGIN / TOKEN - SUCCESS
    // ============================================================

    @Test
    void loginToken_shouldReturnTokenResponse_whenCredentialsAreValid() {

        LoginRequest request = new LoginRequest();

        request.setEmailId("employee@test.com");
        request.setPassword("password");

        when(employee.getPasswordHash()).thenReturn("hashedPassword");
        when(employeeService.getEmployeeByEmail("employee@test.com"))
                .thenReturn(Optional.of(employee));

        when(employee.getIsActive())
                .thenReturn(false);

        when(employee.getFailedLoginAttempts())
                .thenReturn(0);

        when(passwordEncoder.matches(
                "password",
                "hashedPassword"
        )).thenReturn(true);

        when(employee.getOfficialEmail()).thenReturn("employee@test.com");
        when(employee.getRole()).thenReturn(UserRole.admin);
        when(jwtService.generateAccessToken(
                employeeId.toString(),
                "employee@test.com",employee.getRole().name()
        )).thenReturn("access-token");

        when(refreshTokenService.issue(
                employeeId.toString()
        )).thenReturn("refresh-token");

        ResponseEntity<TokenResponse> response =
                authController.login(request);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        verify(employeeService)
                .getEmployeeByEmail("employee@test.com");

        verify(passwordEncoder)
                .matches("password", "hashedPassword");

        verify(employeeService)
                .updateEmployee(employeeId, employee);

        verify(jwtService)
                .generateAccessToken(
                        employeeId.toString(),
                        "employee@test.com",employee.getRole().name()
                );

        verify(refreshTokenService)
                .issue(employeeId.toString());
    }

    // ============================================================
    // LOGIN / TOKEN - USER DOES NOT EXIST
    // ============================================================

    @Test
    void loginToken_shouldThrowUnauthorized_whenEmployeeDoesNotExist() {

        LoginRequest request = new LoginRequest();

        request.setEmailId("unknown@test.com");
        request.setPassword("password");

        when(employeeService.getEmployeeByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> authController.login(request)
        );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                exception.getStatus()
        );

        assertEquals(
                "Invalid username or password",
                exception.getMessage()
        );

        verify(employeeService)
                .getEmployeeByEmail("unknown@test.com");

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(refreshTokenService);
    }

    // ============================================================
    // LOGIN / TOKEN - WRONG PASSWORD
    // ============================================================

    @Test
    void loginToken_shouldThrowUnauthorized_whenPasswordIsWrong() {

        LoginRequest request = new LoginRequest();

        request.setEmailId("employee@test.com");
        request.setPassword("wrongPassword");

        when(employeeService.getEmployeeByEmail("employee@test.com"))
                .thenReturn(Optional.of(employee));

        when(employee.getIsActive())
                .thenReturn(false);

        when(employee.getFailedLoginAttempts())
                .thenReturn(0);

        when(passwordEncoder.matches(
                "wrongPassword",
                "hashedPassword"
        )).thenReturn(false);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> authController.login(request)
        );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                exception.getStatus()
        );

        assertEquals(
                "Invalid username or password",
                exception.getMessage()
        );

        verify(employee)
                .setFailedLoginAttempts(1);

        verify(employeeService)
                .updateEmployee(employeeId, employee);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(refreshTokenService);
    }

    // ============================================================
    // LOGIN / TOKEN - ACCOUNT LOCK AFTER 5 ATTEMPTS
    // ============================================================

    @Test
    void loginToken_shouldLockAccount_afterFiveFailedAttempts() {

        LoginRequest request = new LoginRequest();

        request.setEmailId("employee@test.com");
        request.setPassword("wrongPassword");

        when(employeeService.getEmployeeByEmail("employee@test.com"))
                .thenReturn(Optional.of(employee));

        when(employee.getIsActive())
                .thenReturn(false);

        */
/*
         * Four previous failed attempts.
         * Controller increases this to five.
         *//*

        when(employee.getFailedLoginAttempts())
                .thenReturn(4);

        when(passwordEncoder.matches(
                "wrongPassword",
                "hashedPassword"
        )).thenReturn(false);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> authController.login(request)
        );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                exception.getStatus()
        );

        verify(employee)
                .setFailedLoginAttempts(5);

        verify(employee)
                .setIsActive(true);

        verify(employee)
                .setLockedUntil(any(Instant.class));

        verify(employeeService)
                .updateEmployee(employeeId, employee);
    }

    // ============================================================
    // LOGIN / TOKEN - ACCOUNT ALREADY LOCKED
    // ============================================================

    @Test
    void loginToken_shouldThrowLocked_whenAccountIsLocked() {

        LoginRequest request = new LoginRequest();

        request.setEmailId("employee@test.com");
        request.setPassword("password");

        when(employeeService.getEmployeeByEmail("employee@test.com"))
                .thenReturn(Optional.of(employee));

        when(employee.getIsActive())
                .thenReturn(true);

        when(employee.getLockedUntil())
                .thenReturn(
                        Instant.now().plusSeconds(600)
                );

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> authController.login(request)
        );

        assertEquals(
                HttpStatus.LOCKED,
                exception.getStatus()
        );

        assertEquals(
                "Account locked. Try again later.",
                exception.getMessage()
        );

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(refreshTokenService);
    }

    // ============================================================
    // REFRESH - SUCCESS
    // ============================================================

    @Test
    void refresh_shouldReturnNewTokens_whenRefreshTokenIsValid() {

        String userId = employeeId.toString();

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("old-refresh-token");

        when(refreshTokenService.validateAndConsume(
                "old-refresh-token"
        )).thenReturn(userId);

        when(employeeService.getEmployeeById(employeeId))
                .thenReturn(employee);

        when(employee.getIsActive())
                .thenReturn(true);

        when(jwtService.generateAccessToken(
                employeeId.toString(),
                "employee@test.com",
                "ADMIN"
        )).thenReturn("new-access-token");

        when(refreshTokenService.issue(
                employeeId.toString()
        )).thenReturn("new-refresh-token");

        ResponseEntity<TokenResponse> response =
                authController.refresh(request);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        verify(refreshTokenService)
                .validateAndConsume("old-refresh-token");

        verify(employeeService)
                .getEmployeeById(employeeId);

        verify(jwtService)
                .generateAccessToken(
                        employeeId.toString(),
                        "employee@test.com",
                        "ADMIN"
                );

        verify(refreshTokenService)
                .issue(employeeId.toString());
    }

    // ============================================================
    // REFRESH - INVALID TOKEN
    // ============================================================

    @Test
    void refresh_shouldThrowUnauthorized_whenRefreshTokenIsInvalid() {

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("invalid-token");

        when(refreshTokenService.validateAndConsume(
                "invalid-token"
        )).thenReturn(null);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> authController.refresh(request)
        );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                exception.getStatus()
        );

        assertEquals(
                "Invalid or expired refresh token",
                exception.getMessage()
        );

        verify(refreshTokenService)
                .validateAndConsume("invalid-token");

        verifyNoInteractions(employeeService);
        verifyNoInteractions(jwtService);
    }

    // ============================================================
    // REFRESH - INACTIVE USER
    // ============================================================

    @Test
    void refresh_shouldThrowUnauthorized_whenUserIsInactive() {

        String userId = employeeId.toString();

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");

        when(refreshTokenService.validateAndConsume(
                "refresh-token"
        )).thenReturn(userId);

        when(employeeService.getEmployeeById(employeeId))
                .thenReturn(employee);

        when(employee.getIsActive())
                .thenReturn(false);

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> authController.refresh(request)
        );

        assertEquals(
                HttpStatus.UNAUTHORIZED,
                exception.getStatus()
        );

        assertEquals(
                "Account unavailable",
                exception.getMessage()
        );

        verify(employeeService)
                .getEmployeeById(employeeId);

        verifyNoInteractions(jwtService);
    }

    // ============================================================
    // LOGIN - AUTHENTICATED USER
    // ============================================================

    @Test
    void login_shouldReturnEmployeeDto_whenUserIsAuthenticated() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(sessionService.getCurrentUser())
                .thenReturn(employee);

        when(employeeService.getEmployeeDtoFromEntity(employee))
                .thenReturn(employeeDto);

        ResponseEntity<EmployeeDto> result =
                authController.login(request, response);

        assertEquals(
                HttpStatus.OK,
                result.getStatusCode()
        );

        assertSame(
                employeeDto,
                result.getBody()
        );

        verify(sessionService)
                .getCurrentUser();

        verify(employeeService)
                .getEmployeeDtoFromEntity(employee);
    }

    // ============================================================
    // LOGOUT - VALID TOKEN
    // ============================================================

    @Test
    void logout_shouldRevokeAllSessions_whenTokenIsValid() {

        String userId = employeeId.toString();

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");

        when(refreshTokenService.validateAndConsume(
                "refresh-token"
        )).thenReturn(userId);

        ResponseEntity<Void> response =
                authController.logout(request);

        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );

        verify(refreshTokenService)
                .validateAndConsume("refresh-token");

        verify(refreshTokenService)
                .revokeAllForUser(userId);
    }

    // ============================================================
    // LOGOUT - INVALID TOKEN
    // ============================================================

    @Test
    void logout_shouldReturnNoContent_whenTokenIsInvalid() {

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("invalid-token");

        when(refreshTokenService.validateAndConsume(
                "invalid-token"
        )).thenReturn(null);

        ResponseEntity<Void> response =
                authController.logout(request);

        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );

        verify(refreshTokenService)
                .validateAndConsume("invalid-token");

        verify(
                refreshTokenService,
                never()
        ).revokeAllForUser(anyString());
    }
}*/
