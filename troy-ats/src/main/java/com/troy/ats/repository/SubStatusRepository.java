package com.troy.ats.repository;

import com.troy.ats.entity.SubStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubStatusRepository extends JpaRepository<SubStatus, UUID> {
}