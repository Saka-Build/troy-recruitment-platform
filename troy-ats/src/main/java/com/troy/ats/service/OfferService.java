package com.troy.ats.service;

import com.troy.ats.entity.Offer;
import com.troy.ats.enums.OfferStatus;
import com.troy.ats.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    @Transactional(readOnly = true)
    public Page<Offer> getAllOffers(Pageable pageable) {
        return offerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Offer> getOfferById(UUID id) {
        return offerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Offer> getOfferBySubmissionId(UUID submissionId) {
        return offerRepository.findBySubmissionId(submissionId);
    }

    @Transactional(readOnly = true)
    public List<Offer> getOffersByCandidateId(UUID candidateId) {
        return offerRepository.findByCandidateId(candidateId);
    }

    @Transactional(readOnly = true)
    public List<Offer> getOffersByJobId(UUID jobId) {
        return offerRepository.findByJobId(jobId);
    }

    @Transactional
    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer updateOffer(UUID id, Offer offer) {
        offer.setId(id);
        return offerRepository.save(offer);
    }

    @Transactional
    public void deleteOffer(UUID id) {
        offerRepository.deleteById(id);
    }
}

