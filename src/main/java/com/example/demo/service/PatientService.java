package com.example.demo.service;

import com.example.demo.exceptions.PatientNotFoundException;
import com.example.demo.model.PasswordRequest;
import com.example.demo.model.Patient;
import com.example.demo.model.PatientDTO;
import com.example.demo.model.PatientMapper;
import com.example.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDTO> getPatients() {
        return patientMapper.map(patientRepository.getPatients());
    }

    public PatientDTO getPatientByEmail(String email) {
        return patientMapper.map(patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist")));
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

    public void editPassword(String email, PasswordRequest newPassword) {
        Patient patient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patientRepository.editPassword(patient, newPassword.newPassword());
    }
}
