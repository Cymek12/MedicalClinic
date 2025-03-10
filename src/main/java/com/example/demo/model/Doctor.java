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
}
