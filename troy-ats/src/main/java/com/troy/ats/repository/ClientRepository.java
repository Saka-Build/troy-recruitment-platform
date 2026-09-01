package com.troy.ats.repository;

import com.troy.ats.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {

    long countByIsActive(boolean active);
    boolean existsByEmailIgnoreCase(String email);

    @Override
    @EntityGraph(attributePaths = {"country"})
    Optional<Client> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"country"})
    Page<Client> findAll(Specification<Client> specification, Pageable pageable);

    List<Client> findByIsActive(boolean active);

}