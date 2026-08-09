package com.troy.ats.repository;

import com.troy.ats.entity.SubStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubStatusRepository extends JpaRepository<SubStatus, UUID> {
    List<SubStatus> findByStatusIdAndIsActiveTrueOrderBySortOrder(UUID statusId);
    Optional<SubStatus> findByStatusIdAndName(UUID statusId, String name);
}

