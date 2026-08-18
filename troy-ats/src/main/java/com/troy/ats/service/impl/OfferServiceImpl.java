package com.troy.ats.service.impl;

import com.troy.ats.entity.Offer;
import com.troy.ats.enums.OfferStatus;
import com.troy.ats.repository.OfferRepository;
import com.troy.ats.service.OfferService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("offerService")
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Optional<Offer> getOfferById(UUID id) {
        return offerRepository.findById(id);
    }

    @Override
    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public void deleteOffer(UUID id) {
        offerRepository.deleteById(id);
    }

    @Override
    public long getTotalJoiningTodayForZoneId(ZoneId zoneId) {

        LocalDate today = LocalDate.now(zoneId);
        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return offerRepository.countByJoiningDateGreaterThanEqualAndJoiningDateLessThan(start, end);
    }

    @Override
    public long getTotalOffersByStaus(OfferStatus offerStatus){
        return offerRepository.countByOfferStatus(offerStatus);
    }
}