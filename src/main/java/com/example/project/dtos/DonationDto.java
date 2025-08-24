package com.example.project.dtos;

import com.example.project.entities.Donation;
import com.example.project.enums.DonationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DonationDto {
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
    @NotNull(message = "Donation Type must be provided")
    private DonationType donationType;
    @NotBlank(message = "Receipt number cannot be blank")
    private String receiptNumber;
    private Instant createdOn;

    public static DonationDto toDto(Donation donation) {
        return DonationDto.builder()
                .amount(donation.getAmount())
                .donationType(DonationType.getDonationTypeFromId(donation.getDonationType()))
                .receiptNumber(donation.getReceiptNumber())
                .createdOn(donation.getCreatedOn())
                .build();
    }

    public static Donation toEntity(DonationDto dto) {
        return Donation.builder()
                .amount(dto.getAmount())
                .donationType(dto.donationType.getId())
                .receiptNumber(dto.getReceiptNumber())
                .build();
    }
}
