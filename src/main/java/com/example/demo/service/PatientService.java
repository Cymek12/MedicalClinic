package com.example.demo.service;

import com.example.demo.exceptions.PatientNotFoundException;
import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatients() {
        return patientRepository.getPatients();
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
    }

    public void addPatient(Patient patient) {
        patientRepository.addPatient(patient);
    }

    public void deletePatientByEmail(String email) {
        Patient patient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patientRepository.deletePatient(patient);
    }

    public void editPatient(String email, Patient newPatientData) {
        Patient patient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patientRepository.editPatient(patient, newPatientData);
    }
}
