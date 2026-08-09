package com.troy.ats.repository;

import com.troy.ats.entity.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingRepository extends JpaRepository<Onboarding, UUID> {
    Optional<Onboarding> findByOfferId(UUID offerId);
    Optional<Onboarding> findByCandidateId(UUID candidateId);
}

