package com.troy.ats.repository;

import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubStatusRepository extends JpaRepository<SubStatus, UUID> {

    Optional<SubStatus> findByNameIgnoreCase(String name);
    List<SubStatus> findByStatusIdAndActiveTrue(UUID statusId);
    List<SubStatus> findByActiveTrue();
}