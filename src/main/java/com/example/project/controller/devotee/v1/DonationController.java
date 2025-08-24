package com.example.project.controller.devotee.v1;

import com.example.project.service.DonationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/donation")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @DeleteMapping("/{donationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDonation(@PathVariable("donationId") Long donationId) {
        donationService.deleteDonation(donationId);
    }
}
