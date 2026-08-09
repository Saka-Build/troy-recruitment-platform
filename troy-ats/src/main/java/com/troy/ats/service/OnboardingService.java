package com.troy.ats.service;

import com.troy.ats.entity.Onboarding;
import com.troy.ats.repository.OnboardingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardingRepository onboardingRepository;

    @Transactional(readOnly = true)
    public Page<Onboarding> getAllOnboarding(Pageable pageable) {
        return onboardingRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Onboarding> getOnboardingById(UUID id) {
        return onboardingRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Onboarding> getOnboardingByOfferId(UUID offerId) {
        return onboardingRepository.findByOfferId(offerId);
    }

    @Transactional
    public Onboarding createOnboarding(Onboarding onboarding) {
        return onboardingRepository.save(onboarding);
    }

    @Transactional
    public Onboarding updateOnboarding(UUID id, Onboarding onboarding) {
        onboarding.setId(id);
        return onboardingRepository.save(onboarding);
    }

    @Transactional
    public void deleteOnboarding(UUID id) {
        onboardingRepository.deleteById(id);
    }
}

