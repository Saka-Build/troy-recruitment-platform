package com.troy.ats.repository;

import com.troy.ats.entity.Offer;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    long countByJoiningDateGreaterThanEqualAndJoiningDateLessThan(Instant start, Instant end);
    long countByOfferStatus(OfferStatus offerStatus);
}