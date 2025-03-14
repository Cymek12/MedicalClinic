package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "DOCTORS")
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String specialization;
    @ManyToMany
    private Set<Institution> institutions = new HashSet<>();

    public void updateDoctor(Doctor newDoctorData) {
        this.setEmail(newDoctorData.getEmail());
        this.setPassword(newDoctorData.getPassword());
        this.setFirstName(newDoctorData.getFirstName());
        this.setLastName(newDoctorData.getLastName());
        this.setSpecialization(newDoctorData.getSpecialization());
    }

    public boolean isDoctorDataNull() {
        return this.getEmail() == null ||
                this.getPassword() == null ||
                this.getFirstName() == null ||
                this.getLastName() == null ||
                this.getSpecialization() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor))
            return false;
        Doctor other = (Doctor) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
