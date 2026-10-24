package com.troy.ats.service;

import com.troy.ats.entity.Onboarding;
import com.troy.ats.repository.OnboardingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OnboardingService {

    /**
     *
     * @return
     */
    public List<Onboarding> getAllOnboarding();

    /**
     *
     * @param id
     * @return
     */
    public Optional<Onboarding> getOnboardingById(UUID id);

    /**
     *
     * @param onboarding
     * @return
     */
    public Onboarding createOnboarding(Onboarding onboarding);

    /**
     *
     * @param id
     */
    public void deleteOnboarding(UUID id);
}