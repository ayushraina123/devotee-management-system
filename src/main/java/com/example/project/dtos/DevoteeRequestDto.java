package com.example.project.dtos;

import com.example.project.enums.DonationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevoteeRequestDto {
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private DonationType donationType;
}
