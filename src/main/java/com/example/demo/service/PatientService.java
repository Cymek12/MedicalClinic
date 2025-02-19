package com.example.demo.service;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatients() {
        return patientRepository.getPatients();
    }

    public Patient getPatientByEmail(String email) {
        Optional<Patient> optionalPatient = patientRepository.getPatientByEmail(email);
        if (optionalPatient.isPresent()) {
            return optionalPatient.get();
        }
        return null;
    }

    public void addNewPatient(Patient patient) {
        patientRepository.addNewPatient(patient);
    }

    public boolean deletePatientByEmail(String email) {
        Optional<Patient> optionalPatient = patientRepository.getPatientByEmail(email);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patientRepository.getPatients().remove(patient);
            return true;
        }
        return false;
    }

    public boolean editPatient(String email, Patient newPatient) {
        Optional<Patient> optionalPatient = patientRepository.getPatientByEmail(email);
        if(optionalPatient.isPresent()){
            Patient patient = optionalPatient.get();
            patient.setEmail(newPatient.getEmail());
            patient.setPassword(newPatient.getPassword());
            patient.setIdCardNo(newPatient.getIdCardNo());
            patient.setFirstName(newPatient.getFirstName());
            patient.setLastName(newPatient.getLastName());
            patient.setPhoneNumber(newPatient.getPhoneNumber());
            patient.setBirthday(newPatient.getBirthday());
            return true;
        }
        return false;
    }
}
