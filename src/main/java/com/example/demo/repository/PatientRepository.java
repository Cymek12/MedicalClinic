package com.example.demo.repository;

import com.example.demo.exceptions.CannotChangeIdCardNoException;
import com.example.demo.exceptions.PatientAlreadyExistException;
import com.example.demo.exceptions.PatientDataIsNullException;
import com.example.demo.model.Patient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepository {
    private final List<Patient> patients;


    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findAny();
    }

    public void addPatient(Patient patient) {
        if (getPatientByEmail(patient.getEmail()).isPresent()) {
            throw new PatientAlreadyExistException("Patient with email: " + patient.getEmail() + " already exist");
        }
        patients.add(patient);
    }

    public void deletePatient(Patient patient) {
        patients.remove(patient);
    }

    public void editPatient(Patient patient, Patient newPatientData) {
        if(!patient.getIdCardNo().equals(newPatientData.getIdCardNo())){
            throw new CannotChangeIdCardNoException("Id card number is unchangeable");
        }
        if(isPatientDataNull(newPatientData)){
            throw new PatientDataIsNullException("Patient fields cannot be null");
        }
        patient.setEmail(newPatientData.getEmail());
        patient.setPassword(newPatientData.getPassword());
        patient.setFirstName(newPatientData.getFirstName());
        patient.setLastName(newPatientData.getLastName());
        patient.setPhoneNumber(newPatientData.getPhoneNumber());
        patient.setBirthday(newPatientData.getBirthday());
    }

    public boolean isPatientDataNull(Patient patient){
         return patient.getEmail() == null ||
                patient.getPassword() == null ||
                patient.getIdCardNo() == null ||
                patient.getFirstName() == null ||
                patient.getLastName() == null ||
                patient.getPhoneNumber() == null ||
                patient.getBirthday() == null;
    }

    public void editPassword(Patient patient, String newPassword) {
        patient.setPassword(newPassword);
    }
}
