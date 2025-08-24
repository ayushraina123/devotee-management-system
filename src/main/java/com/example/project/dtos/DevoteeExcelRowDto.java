package com.example.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevoteeExcelRowDto {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String city;
    private String state;
    private String country;
    private int pincode;
    private String donations;
    private BigDecimal totalDonation;
}
