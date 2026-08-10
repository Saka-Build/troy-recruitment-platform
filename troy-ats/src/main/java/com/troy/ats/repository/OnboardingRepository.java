package com.troy.ats.repository;

import com.troy.ats.entity.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OnboardingRepository extends JpaRepository<Onboarding, UUID> {
}