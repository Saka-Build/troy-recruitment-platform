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

public interface OfferService {

    /**
     *
     * @return
     */
    public List<Offer> getAllOffers();

    /**
     *
     * @param id
     * @return
     */
    public Optional<Offer> getOfferById(UUID id);

    /**
     *
     * @param offer
     * @return
     */
    public Offer createOffer(Offer offer);

    /**
     *
     * @param id
     */
    public void deleteOffer(UUID id);

    /**
     *
     * @param zoneId
     * @return
     */
    public long getTotalJoiningTodayForZoneId(ZoneId zoneId);

    /**
     *
     * @param offerStatus
     * @return
     */
    public long getTotalOffersByStaus(OfferStatus offerStatus);
}