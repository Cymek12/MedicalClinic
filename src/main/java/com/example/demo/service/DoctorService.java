package com.example.demo.service;

import com.example.demo.exceptions.DoctorAlreadyExistException;
import com.example.demo.exceptions.DoctorDataIsNullException;
import com.example.demo.exceptions.DoctorNotFoundException;
import com.example.demo.exceptions.InstitutionNotFoundException;
import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorDTO;
import com.example.demo.model.DoctorMapper;
import com.example.demo.model.Institution;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final InstitutionRepository institutionRepository;

    public List<DoctorDTO> getDoctors() {
        return doctorMapper.map(doctorRepository.findAll());
    }

    public DoctorDTO getDoctorByEmail(String email) {
        return doctorMapper.map(doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists")));
    }

    public void addDoctor(Doctor doctor) {
        validateAddingDoctor(doctor);
        doctorRepository.save(doctor);
    }

    private void validateAddingDoctor(Doctor doctor) {
        if (isDoctorDataNull(doctor)) {
            throw new DoctorDataIsNullException("Doctor fields cannot be null");
        }
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DoctorAlreadyExistException("Doctor with email: " + doctor.getEmail() + " already exists");
        }
    }

    public void deleteDoctorByEmail(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists"));
        doctorRepository.delete(doctor);
    }

    public void editDoctorByEmail(String email, Doctor newDoctorData) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists"));
        validateNewDoctorData(doctor, newDoctorData);
        editDoctorData(doctor, newDoctorData);
        doctorRepository.save(doctor);
    }

    private void validateNewDoctorData(Doctor doctor, Doctor newDoctorData) {
        if (isDoctorDataNull(doctor)) {
            throw new DoctorDataIsNullException("Doctor fields cannot be null");
        }
        if (doctorRepository.existsByEmail(doctor.getEmail()) && !doctor.getEmail().equals(newDoctorData.getEmail())) {
            throw new DoctorAlreadyExistException("Doctor with email: " + newDoctorData.getEmail() + " already exists");
        }
    }

    private void editDoctorData(Doctor doctor, Doctor newDoctorData) {
        doctor.setEmail(newDoctorData.getEmail());
        doctor.setPassword(newDoctorData.getPassword());
        doctor.setFirstName(newDoctorData.getFirstName());
        doctor.setLastName(newDoctorData.getLastName());
        doctor.setSpecialization(newDoctorData.getSpecialization());
    }

    private boolean isDoctorDataNull(Doctor doctor) {
        return doctor.getEmail() == null ||
                doctor.getPassword() == null ||
                doctor.getFirstName() == null ||
                doctor.getLastName() == null ||
                doctor.getSpecialization() == null;
    }

    public void addInstitution(String email, String id){
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists"));
        Institution institution = institutionRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new InstitutionNotFoundException("Instituion with id: " + id + " do not exists"));
        doctor.getInstitutions().add(institution);
        doctorRepository.save(doctor);

    }

}
