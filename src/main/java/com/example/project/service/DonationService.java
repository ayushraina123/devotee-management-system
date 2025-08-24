package com.example.project.service;

import com.example.project.entities.Devotee;
import com.example.project.repositories.DonationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final DevoteeService devoteeService;

    public DonationService(DonationRepository donationRepository, DevoteeService devoteeService) {
        this.donationRepository = donationRepository;
        this.devoteeService = devoteeService;
    }

    public BigDecimal getDonationSum() {
        return donationRepository.getTotalDonationsAmount();
    }

    public void deleteDonation(Long donationId) {
        Devotee devotee = devoteeService.findDevoteeByDonation(donationId);
        devotee.getDonations().removeIf(d -> d.getId().equals(donationId));

        if (devotee.getDonations().isEmpty()) {
            devoteeService.deleteDevotee(devotee.getId());
        } else {
            devoteeService.saveUpdatedDevotee(devotee);
        }
    }
}
