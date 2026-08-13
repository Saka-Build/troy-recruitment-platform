package com.troy.ats.service.impl;

import com.troy.ats.entity.Employee;
import com.troy.ats.service.SessionService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("sessionService")
public class SessionServiceImpl implements SessionService {
    /**
     * current session user
     *
     * @return
     */
    @Override
    public Employee getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("User is not authenticated");
        }

        return (Employee) authentication.getPrincipal();
    }
}
