package com.example.demo.service;

import com.example.demo.exceptions.*;
import com.example.demo.model.dto.PageContentDTO;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.model.*;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDTO> getPatients() {
        return patientMapper.map(patientRepository.findAll());
    }

    public PageContentDTO<PatientDTO> getPatients(Pageable pageable) {
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        List<PatientDTO> patientDTOS = patientMapper.map(patientPage.getContent());
        return new PageContentDTO<>(
                patientPage.getTotalElements(),
                patientPage.getNumber(),
                patientPage.getTotalPages(),
                patientDTOS
        );
    }

    public PatientDTO getPatientByEmail(String email) {
        return patientMapper.map(patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist")));
    }

    public void addPatient(Patient patient) {
        validateAddingPatient(patient);
        patientRepository.save(patient);
    }

    private void validateAddingPatient(Patient patient) {
        if (patient.isPatientDataNull()) {
            throw new PatientDataIsNullException("Patient fields cannot be null");
        }
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new PatientAlreadyExistException("Patient with email: " + patient.getEmail() + " already exist");
        }
    }

    public void deletePatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patientRepository.delete(patient);
    }

    public void editPatient(String email, Patient newPatientData) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        validateNewPatientData(patient, newPatientData);
        patient.editPatientData(newPatientData);
        patientRepository.save(patient);
    }

    public void editPassword(String email, PasswordRequest passwordRequest) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        patient.setPassword(passwordRequest.newPassword());
        patientRepository.save(patient);
    }

    private void validateNewPatientData(Patient patient, Patient newPatientData) {
        if (!patient.getIdCardNo().equals(newPatientData.getIdCardNo())) {
            throw new CannotChangeIdCardNoException("Id card number is unchangeable");
        }
        if (newPatientData.isPatientDataNull()) {
            throw new PatientDataIsNullException("Patient fields cannot be null");
        }
        if (patientRepository.existsByEmail(patient.getEmail()) && !patient.getEmail().equals(newPatientData.getEmail())) {
            throw new PatientAlreadyExistException("Patient with email: " + newPatientData.getEmail() + " already exist");
        }
    }
}
