package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private List<Patient> patients;

    public PatientRepository(List<Patient> patients) {
        this.patients = new ArrayList<>();
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public Optional<Patient> getPatientByEmail(String email){
        return patients.stream().filter(patient -> patient.getEmail().equals(email)).findAny();
    }

    public void addNewPatient(Patient patient){
        patients.add(patient);
    }
}
