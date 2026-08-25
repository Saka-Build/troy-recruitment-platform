package com.troy.ats.service.jwt;

import com.troy.ats.entity.Employee;
import com.troy.ats.service.EmployeeAuthorizationService;
import com.troy.ats.service.EmployeeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final EmployeeService employeeService;
    private final EmployeeAuthorizationService authorizationService;

    public JwtAuthFilter(
            JwtService jwtService,
            EmployeeService employeeService,
            EmployeeAuthorizationService authorizationService
    ) {
        this.jwtService = jwtService;
        this.employeeService = employeeService;
        this.authorizationService = authorizationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                                    throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {

            String token = header.substring(7);

            // 1. Validate JWT
            Claims claims = jwtService.parseAndValidate(token);

            // 2. Subject contains employee UUID
            UUID userId = UUID.fromString(claims.getSubject());

            // 3. Load employee
            Employee user = employeeService.getEmployeeById(userId);

            // 4. Make sure employee is active
            if (!Boolean.TRUE.equals(user.getIsActive())) {
                SecurityContextHolder.clearContext();
                chain.doFilter(request, response);
                return;
            }

            // 5. Load current roles + permissions from DB
            var authorities = authorizationService.getAuthorities(userId);

            // 6. Create Spring Security authentication
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            // 7. Store authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println(
                    "Authenticated: " +
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .isAuthenticated());

        } catch (JwtException | IllegalArgumentException  e) {

            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}