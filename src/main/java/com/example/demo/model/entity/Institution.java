package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String zipCode;
    private String street;
    private String buildingNumber;
    @ManyToMany(mappedBy = "institutions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Doctor> doctors;

    public void editInstitutionData(Institution newInstitutionData) {
        this.setName(newInstitutionData.getName());
        this.setCity(newInstitutionData.getCity());
        this.setZipCode(newInstitutionData.getZipCode());
        this.setStreet(newInstitutionData.getStreet());
        this.setBuildingNumber(newInstitutionData.getBuildingNumber());
    }

    public boolean isInstitutionDataNull() {
        return Objects.isNull(this.getName()) ||
                Objects.isNull(this.getCity()) ||
                Objects.isNull(this.getZipCode()) ||
                Objects.isNull(this.getStreet()) ||
                Objects.isNull(this.getBuildingNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institution))
            return false;
        Institution other = (Institution) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
