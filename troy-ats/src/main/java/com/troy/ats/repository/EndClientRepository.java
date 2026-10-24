package com.troy.ats.repository;

import com.troy.ats.entity.EndClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EndClientRepository extends JpaRepository<EndClient, UUID>{
    List<EndClient> findByActiveTrueOrderByNameAsc();
}