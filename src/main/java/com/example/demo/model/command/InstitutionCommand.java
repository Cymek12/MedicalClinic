package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InstitutionCommand {
    private String name;
    private String city;
    private String zipCode;
    private String street;
    private String buildingNumber;
}
