package com.troy.ats.service.impl;

import com.troy.ats.entity.Country;
import com.troy.ats.repository.CountryRepository;
import com.troy.ats.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("countryService")
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Country getCountryById(UUID id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with code: " + id));
    }

    /**
     *
     * @param code
     * @return
     */
    @Override
    public Country getCountryByCode(String code) {
        return countryRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Country not found with code: " + code));
    }

    /**
     *
     * @return
     */
    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }
}
