package com.troy.ats.controller;

import com.troy.ats.entity.Country;
import com.troy.ats.populator.DashBoardSummaryPopulator;
import com.troy.ats.service.impl.CountryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/global")
public class GlobalController {

    private final CountryServiceImpl countryService;

    public GlobalController(CountryServiceImpl countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/getCountries")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

}