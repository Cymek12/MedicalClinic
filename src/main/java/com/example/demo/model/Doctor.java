package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "DOCTORS")
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
    private List<Institution> institutions;

    public void updateDoctor(Doctor newDoctorData){
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
        if (!(o instanceof Doctor doctor)) return false;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
