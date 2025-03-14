package com.example.demo.service;

import com.example.demo.exceptions.*;
import com.example.demo.model.DoctorRequest;
import com.example.demo.model.dto.FullInstitutionDTO;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.model.dto.PageContentDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Institution;
import com.example.demo.model.mapper.InstitutionMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;

    public PageContentDTO<InstitutionDTO> getInstitutions(Pageable pageable) {
        Page<Institution> institutionPage = institutionRepository.findAll(pageable);
        List<InstitutionDTO> institutionDTOS = institutionMapper.map(institutionPage.getContent());
        return new PageContentDTO<>(
                institutionPage.getTotalElements(),
                institutionPage.getNumber(),
                institutionPage.getTotalPages(),
                institutionDTOS
        );
    }

    public InstitutionDTO getInstitutionByName(String name) {
        return institutionMapper.map(institutionRepository.findByName(name)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution with name: " + name + " do not exists")));
    }

    public void addInstitution(Institution institution) {
        validateAddingInstitution(institution);
        institutionRepository.save(institution);
    }

    public void addInstitutionWithDoctors(FullInstitutionDTO fullInstitutionDTO) {
        Institution institution = prepareInstitution(fullInstitutionDTO);
        assignDoctorToInstitution(institution, fullInstitutionDTO.getDoctors());
        institutionRepository.save(institution);
    }

    public void addInstitutionsWithDoctors(List<FullInstitutionDTO> fullInstitutionDTOS) {
        Set<Institution> institutions = fullInstitutionDTOS.stream()
                .map(fullInstitutionDTO -> {
                    Institution institution = prepareInstitution(fullInstitutionDTO);
                    assignDoctorToInstitution(institution, fullInstitutionDTO.getDoctors());
                    return institution;
                })
                .collect(Collectors.toSet());
        institutionRepository.saveAll(institutions);
    }

    private void assignDoctorToInstitution(Institution institution, Set<DoctorRequest> doctorRequests) {
        Set<Doctor> doctors = new HashSet<>();
        if (!doctorRequests.isEmpty()) {
            for (DoctorRequest doctorRequest : doctorRequests) {
                Doctor doctor = doctorRepository.findByEmail(doctorRequest.email())
                        .orElseGet(() -> {
                            Doctor createdDoctor = Doctor.builder()
                                    .firstName(doctorRequest.firstName())
                                    .lastName(doctorRequest.lastName())
                                    .email(doctorRequest.email())
                                    .password(doctorRequest.password())
                                    .specialization(doctorRequest.specialization())
                                    .institutions(new HashSet<>())
                                    .build();
                            doctorService.validateAddingDoctor(createdDoctor);
                            return createdDoctor;
                        });
                doctors.add(doctor);
            }
        }
        doctors.forEach(doctor -> doctor.getInstitutions().add(institution));
        institution.getDoctors().addAll(doctors);
    }

    private Institution prepareInstitution(FullInstitutionDTO fullInstitutionDTORequest) {
        return institutionRepository.findByName(fullInstitutionDTORequest.getName())
                .orElseGet(() -> {
                    Institution institution = Institution.builder()
                            .name(fullInstitutionDTORequest.getName())
                            .city(fullInstitutionDTORequest.getCity())
                            .zipCode(fullInstitutionDTORequest.getZipCode())
                            .street(fullInstitutionDTORequest.getStreet())
                            .buildingNumber(fullInstitutionDTORequest.getBuildingNumber())
                            .doctors(new HashSet<>())
                            .build();
                    validateAddingInstitution(institution);
                    return institution;
                });
    }

    private void validateAddingInstitution(Institution institution) {
        if (institution.isInstitutionDataNull()) {
            throw new InstitutionDataIsNullException("Institution fields cannot be null");
        }
        if (institutionRepository.existsByName(institution.getName())) {
            throw new InstitutionAlreadyExistException("Institution with name: " + institution.getName() + " already exists");
        }
    }

    public void deleteInstitutionByName(String name) {
        Institution institution = institutionRepository.findByName(name)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution with name: " + name + " do not exists"));
        institutionRepository.delete(institution);
    }

    public void editInstitutionByName(String name, Institution newInstitutionData) {
        Institution institution = institutionRepository.findByName(name)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution with name: " + name + " do not exists"));
        validateNewInstitutionData(institution, newInstitutionData);
        institution.editInstitutionData(newInstitutionData);
        institutionRepository.save(institution);
    }

    private void validateNewInstitutionData(Institution institution, Institution newInstitutionData) {
        if (newInstitutionData.isInstitutionDataNull()) {
            throw new InstitutionDataIsNullException("Institution fields cannot be null");
        }
        if (institutionRepository.existsByName(institution.getName()) && !institution.getName().equals(newInstitutionData.getName())) {
            throw new InstitutionNotFoundException("Institution with name: " + newInstitutionData.getName() + " do not exists");
        }
    }
}
