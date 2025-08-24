package com.example.project.dtos;

import com.example.project.entities.Devotee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class DevoteeDto {
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotBlank(message = "Father name cannot be blank")
    private String fatherName;
    @Valid
    @NotNull(message = "Address is required")
    private AddressDto address;
    @Valid
    @NotEmpty(message = "Donation is required")
    private List<DonationDto> donation;

    public static Page<DevoteeDto> buildDevoteeDtos(Page<Devotee> devotees, Integer donationTypeId) {
        return devotees.map(devotee -> DevoteeDto.builder()
                .firstName(devotee.getFirstName())
                .lastName(devotee.getLastName())
                .fatherName(devotee.getFatherName())
                .donation(devotee.getDonations().stream()
                        .filter(donation -> donationTypeId == null || donation.getDonationType().equals(donationTypeId))
                        .map(DonationDto::toDto)
                        .toList())
                .address(AddressDto.toDto(devotee.getAddress()))
                .build());
    }

    public static Devotee buildDevotees(DevoteeDto devoteeDto) {
        return Devotee.builder()
                .firstName(devoteeDto.getFirstName())
                .lastName(devoteeDto.getLastName())
                .fatherName(devoteeDto.getFatherName())
                .donations(devoteeDto.getDonation().stream().map(DonationDto::toEntity).toList())
                .build();
    }

    public static Devotee updateDevotee(Devotee devotee, DevoteeDto devoteeDto, boolean isAddressSame) {
        if (!isAddressSame) {
            devotee.setAddress(AddressDto.toEntity(devoteeDto.getAddress()));
        }
        devotee.getDonations().addAll(devoteeDto.getDonation().stream().map(DonationDto::toEntity).toList());
        return devotee;
    }
}