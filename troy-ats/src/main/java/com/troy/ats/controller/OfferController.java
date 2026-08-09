package com.troy.ats.controller;

import com.troy.ats.entity.Offer;
import com.troy.ats.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<Page<Offer>> getAllOffers(Pageable pageable) {
        return ResponseEntity.ok(offerService.getAllOffers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable UUID id) {
        return offerService.getOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<Offer> getOfferBySubmissionId(@PathVariable UUID submissionId) {
        return offerService.getOfferBySubmissionId(submissionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

