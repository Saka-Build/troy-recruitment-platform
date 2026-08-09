package com.troy.ats.controller;

import com.troy.ats.entity.Onboarding;
import com.troy.ats.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @GetMapping
    public ResponseEntity<Page<Onboarding>> getAllOnboarding(Pageable pageable) {
        return ResponseEntity.ok(onboardingService.getAllOnboarding(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Onboarding> getOnboardingById(@PathVariable UUID id) {
        return onboardingService.getOnboardingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/offer/{offerId}")
    public ResponseEntity<Onboarding> getOnboardingByOfferId(@PathVariable UUID offerId) {
        return onboardingService.getOnboardingByOfferId(offerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

