package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String zipCode;
    private String street;
    private String buildingNumber;
    @ManyToMany(mappedBy = "institutions")
    private List<Doctor> doctors;


    public void editInstitutionData(Institution newInstitutionData) {
        this.setName(newInstitutionData.getName());
        this.setCity(newInstitutionData.getCity());
        this.setZipCode(newInstitutionData.getZipCode());
        this.setStreet(newInstitutionData.getStreet());
        this.setBuildingNumber(newInstitutionData.getBuildingNumber());
    }

    public boolean isInstitutionDataNull() {
        return this.getName() == null ||
                this.getCity() == null ||
                this.getZipCode() == null ||
                this.getStreet() == null ||
                this.getBuildingNumber() == null;
    }
}
