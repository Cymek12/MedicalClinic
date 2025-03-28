package com.example.demo.model.dto;

import com.example.demo.model.command.DoctorCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class FullInstitutionDTO {
    private String name;
    private String city;
    private String zipCode;
    private String street;
    private String buildingNumber;
    private Set<DoctorCommand> doctors;
}
