package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
        return Objects.isNull(this.getEmail()) ||
                Objects.isNull(this.getPassword()) ||
                Objects.isNull(this.getIdCardNo()) ||
                Objects.isNull(this.getFirstName()) ||
                Objects.isNull(this.getLastName()) ||
                Objects.isNull(this.getPhoneNumber()) ||
                Objects.isNull(this.getBirthday());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient))
            return false;
        Patient other = (Patient) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
