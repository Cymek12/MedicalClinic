package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientDAO extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    void delete(Patient patient);
}
