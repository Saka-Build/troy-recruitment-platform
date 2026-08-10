package com.troy.ats.service;

import com.troy.ats.entity.Onboarding;
import com.troy.ats.repository.OnboardingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OnboardingService {

    private final OnboardingRepository onboardingRepository;

    public OnboardingService(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }

    public List<Onboarding> getAllOnboarding() {
        return onboardingRepository.findAll();
    }

    public Optional<Onboarding> getOnboardingById(UUID id) {
        return onboardingRepository.findById(id);
    }

    public Onboarding createOnboarding(Onboarding onboarding) {
        return onboardingRepository.save(onboarding);
    }

    public void deleteOnboarding(UUID id) {
        onboardingRepository.deleteById(id);
    }
}