package com.troy.ats.service;

import com.troy.ats.entity.Interview;
import com.troy.ats.entity.Offer;
import com.troy.ats.enums.OfferStatus;
import com.troy.ats.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(UUID id) {
        return offerRepository.findById(id);
    }

    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public void deleteOffer(UUID id) {
        offerRepository.deleteById(id);
    }

    public long getTotalJoiningTodayForZoneId(ZoneId zoneId) {

        LocalDate today = LocalDate.now(zoneId);
        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return offerRepository.countByJoiningDateGreaterThanEqualAndJoiningDateLessThan(start, end);
    }

    public long getTotalOffersByStaus(OfferStatus offerStatus){
        return offerRepository.countByOfferStatus(offerStatus);
    }
}