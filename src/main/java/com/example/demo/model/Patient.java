package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    public void editPatientData(Patient newPatientData) {
        this.setEmail(newPatientData.getEmail());
        this.setPassword(newPatientData.getPassword());
        this.setFirstName(newPatientData.getFirstName());
        this.setLastName(newPatientData.getLastName());
        this.setPhoneNumber(newPatientData.getPhoneNumber());
        this.setBirthday(newPatientData.getBirthday());
    }

    public boolean isPatientDataNull() {
        return this.getEmail() == null ||
                this.getPassword() == null ||
                this.getIdCardNo() == null ||
                this.getFirstName() == null ||
                this.getLastName() == null ||
                this.getPhoneNumber() == null ||
                this.getBirthday() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
