package com.example.project.repositories;

import com.example.project.entities.Devotee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevoteeRepository extends JpaRepository<Devotee, Long>, JpaSpecificationExecutor<Devotee> {
    @EntityGraph(attributePaths = {"address", "donations"})
    Page<Devotee> findAll(Specification<Devotee> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"address", "donations"})
    List<Devotee> findAll();

    @EntityGraph(attributePaths = {"address", "donations"})
    Optional<Devotee> findByDonations_Id(Long donationId);

    @EntityGraph(attributePaths = {"address", "donations"})
    boolean existsByAddress_Id(Long addressId);
}
