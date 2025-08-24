package com.example.project.dtos;

import com.example.project.entities.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {
    @NotBlank(message = "House Number cannot be blank")
    private String houseNumber;
    @NotBlank(message = "City cannot be blank")
    private String city;
    @NotBlank(message = "State cannot be blank")
    private String state;
    @NotBlank(message = "Country cannot be blank")
    private String country;
    @NotNull(message = "Pincode cannot be blank")
    @Min(value = 1, message = "Pincode must be greater than 0")
    private Integer pincode;

    public static Address toEntity(AddressDto dto) {
        return Address.builder()
                .houseNumber(dto.getHouseNumber())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .pincode(dto.getPincode())
                .build();
    }

    public static AddressDto toDto(Address entity) {
        return AddressDto.builder()
                .houseNumber(entity.getHouseNumber())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .pincode(entity.getPincode())
                .build();
    }
}
