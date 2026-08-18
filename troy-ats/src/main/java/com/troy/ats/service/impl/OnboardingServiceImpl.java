package com.troy.ats.service.impl;

import com.troy.ats.entity.Onboarding;
import com.troy.ats.repository.OnboardingRepository;
import com.troy.ats.service.OnboardingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("onboardingService")
public class OnboardingServiceImpl implements OnboardingService {

    private final OnboardingRepository onboardingRepository;

    public OnboardingServiceImpl(OnboardingRepository onboardingRepository) {
        this.onboardingRepository = onboardingRepository;
    }

    @Override
    public List<Onboarding> getAllOnboarding() {
        return onboardingRepository.findAll();
    }

    @Override
    public Optional<Onboarding> getOnboardingById(UUID id) {
        return onboardingRepository.findById(id);
    }

    @Override
    public Onboarding createOnboarding(Onboarding onboarding) {
        return onboardingRepository.save(onboarding);
    }

    @Override
    public void deleteOnboarding(UUID id) {
        onboardingRepository.deleteById(id);
    }
}