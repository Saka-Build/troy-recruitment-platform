package com.troy.ats.controller;

import com.troy.ats.entity.Onboarding;
import com.troy.ats.service.OnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @GetMapping
    public List<Onboarding> getAllOnboarding() {
        return onboardingService.getAllOnboarding();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Onboarding> getOnboardingById(@PathVariable Long id) {
        return onboardingService.getOnboardingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Onboarding createOnboarding(@RequestBody Onboarding onboarding) {
        return onboardingService.createOnboarding(onboarding);
    }
}