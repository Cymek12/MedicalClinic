package com.example.demo.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InstitutionDTO {
    private Long id;
    private String name;
    private String city;
    private String zipCode;
    private String street;
    private String buildingNumber;
    private List<Long> doctorIds;
}
