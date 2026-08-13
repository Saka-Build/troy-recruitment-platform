package com.troy.ats.service.jwt;

import com.troy.ats.entity.Employee;
import com.troy.ats.service.EmployeeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Stateless: every instance validates the JWT signature/expiry itself,
 * no DB or Redis lookup on the hot path.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final EmployeeService employeeService;

    public JwtAuthFilter(JwtService jwtService, EmployeeService employeeService) {
        this.jwtService = jwtService;
        this.employeeService = employeeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        //boolean isLoginUrl = request.getRequestURI().contains("login");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {


            String token = header.substring(7);
            Claims claims = jwtService.parseAndValidate(token);
            String role = claims.get("role", String.class);

            /*var auth = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );*/

            UUID userId = UUID.fromString(claims.getSubject());

            Employee user = employeeService.getEmployeeById(userId)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "User not found: " + userId
                            )
                    );
            var auth = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JwtException e) {
            // invalid/expired token -> leave context unauthenticated,
            // downstream endpoints protected by SecurityConfig will 401
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private void authenticateAllApiExceptLogin() {
        var auth = new UsernamePasswordAuthenticationToken(
                "userId",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}