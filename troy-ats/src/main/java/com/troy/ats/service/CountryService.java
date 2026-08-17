package com.troy.ats.service;

import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.EmployeesFiltersDto;
import com.troy.ats.entity.Country;
import com.troy.ats.entity.Employee;
import com.troy.ats.searchfilter.dto.EmployeeExportFilter;
import com.troy.ats.searchfilter.dto.EmployeeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryService {

    /**
     *
     * @param id
     * @return
     */
    public Country getCountryById(UUID id) ;

    /**
     *
     * @param code
     * @return
     */
    public Country getCountryByCode(String code) ;

}

