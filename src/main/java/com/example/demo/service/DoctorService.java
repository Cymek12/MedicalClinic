package com.example.demo.service;

import com.example.demo.exceptions.DoctorAlreadyExistException;
import com.example.demo.exceptions.DoctorDataIsNullException;
import com.example.demo.exceptions.DoctorNotFoundException;
import com.example.demo.exceptions.InstitutionNotFoundException;
import com.example.demo.model.dto.PageContentDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.model.entity.Institution;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final InstitutionRepository institutionRepository;

    public PageContentDTO<DoctorDTO> getDoctors(Pageable pageable) {
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        List<DoctorDTO> doctorDTOS = doctorMapper.map(doctorPage.getContent());
        return new PageContentDTO<>(
                doctorPage.getTotalElements(),
                doctorPage.getNumber(),
                doctorPage.getTotalPages(),
                doctorDTOS
        );
    }

    public DoctorDTO getDoctorByEmail(String email) {
        return doctorMapper.map(doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists")));
    }

    public void addDoctor(Doctor doctor) {
        validateAddingDoctor(doctor);
        doctorRepository.save(doctor);
    }

    public void validateAddingDoctor(Doctor doctor) {
        if (doctor.isDoctorDataNull()) {
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
        doctor.updateDoctor(newDoctorData);
        doctorRepository.save(doctor);
    }

    private void validateNewDoctorData(Doctor doctor, Doctor newDoctorData) {
        if (doctor.isDoctorDataNull()) {
            throw new DoctorDataIsNullException("Doctor fields cannot be null");
        }
        if (doctorRepository.existsByEmail(doctor.getEmail()) && !doctor.getEmail().equals(newDoctorData.getEmail())) {
            throw new DoctorAlreadyExistException("Doctor with email: " + newDoctorData.getEmail() + " already exists");
        }
    }

    public void addInstitution(String email, String id) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists"));
        Institution institution = institutionRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new InstitutionNotFoundException("Instituion with id: " + id + " do not exists"));
        doctor.getInstitutions().add(institution);
        doctorRepository.save(doctor);
    }
}
