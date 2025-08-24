package com.example.project.repositories;

import com.example.project.entities.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d")
    BigDecimal getTotalDonationsAmount();
}
