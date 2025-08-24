package com.example.project.batch.steps;

import com.example.project.dtos.DevoteeExcelRowDto;
import com.example.project.entities.Address;
import com.example.project.entities.Devotee;
import com.example.project.entities.Donation;
import com.example.project.enums.DonationType;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@StepScope
public class DevoteeProcessor implements ItemProcessor<Devotee, DevoteeExcelRowDto> {

    @Value("#{jobParameters['donationType']}")
    private String donationTypeParam;

    @Override
    public DevoteeExcelRowDto process(Devotee devotee) {
        DonationType donationType = DonationType.valueOf(donationTypeParam.toUpperCase());
        int donationTypeId = donationType.getId();

        List<Donation> donationList = devotee.getDonations() == null ? List.of()
                : devotee.getDonations().stream()
                .filter(x -> Objects.equals(x.getDonationType(), donationTypeId))
                .toList();

        String donationsStr = donationList.stream()
                .map(donation -> "Rs. " + donation.getAmount() + "  " + "Receipt Number : " + donation.getReceiptNumber()
                        + " Date Time : " + donation.getCreatedOn())
                .collect(Collectors.joining(", "));
        BigDecimal totalDonation = donationList.stream().map(Donation::getAmount).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Address address = devotee.getAddress();
        return new DevoteeExcelRowDto(
                devotee.getFirstName(),
                devotee.getLastName(),
                devotee.getFatherName(),
                address != null ? address.getCity() : "",
                address != null ? address.getState() : "",
                address != null ? address.getCountry() : "",
                address != null ? address.getPincode() : 0,
                donationsStr,
                totalDonation
        );
    }
}
