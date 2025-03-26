package com.example.demo.service;

import com.example.demo.exceptions.institution.InstitutionAlreadyExistException;
import com.example.demo.exceptions.institution.InstitutionDataIsNullException;
import com.example.demo.exceptions.institution.InstitutionNotFoundException;
import com.example.demo.model.DoctorRequest;
import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.command.InstitutionCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.FullInstitutionDTO;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.model.PageContent;
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

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;

    public PageContent<InstitutionDTO> getInstitutions(Pageable pageable) {
        Page<Institution> institutionPage = institutionRepository.findAll(pageable);
        List<InstitutionDTO> institutionDTOS = institutionMapper.toDTO(institutionPage.getContent());
        return new PageContent<>(
                institutionPage.getTotalElements(),
                institutionPage.getNumber(),
                institutionPage.getTotalPages(),
                institutionDTOS
        );
    }

    public InstitutionDTO getInstitutionByName(String name) {
        return institutionMapper.toDTO(institutionRepository.findByName(name)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution with name: " + name + " do not exists")));
    }

    public InstitutionDTO addInstitution(InstitutionCommand institutionCommand) {
        Institution institution = institutionMapper.toEntity(institutionCommand);
        validateAddingInstitution(institution);
        Institution savedInstitution = institutionRepository.save(institution);
        return institutionMapper.toDTO(savedInstitution);
    }

    public void addInstitutionsWithDoctors(List<FullInstitutionDTO> fullInstitutionDTOS) {
        Set<Institution> institutions = new HashSet<>();
        fullInstitutionDTOS.forEach(fullInstitutionDTO -> {
            Institution institution = prepareInstitution(fullInstitutionDTO);
            assignDoctorToInstitution(institution, fullInstitutionDTO.getDoctors());
            institutions.add(institution);
        });
        institutionRepository.saveAll(institutions);
    }

    private void assignDoctorToInstitution(Institution institution, Set<DoctorCommand> doctorCommandSet) {
        Set<Doctor> doctors = new HashSet<>();
        if (!doctorCommandSet.isEmpty()) {
            doctorCommandSet.forEach(doctorCommand -> {
                Doctor doctor = doctorRepository.findByEmail(doctorCommand.getEmail())
                        .orElseGet(() -> {
                            Doctor createdDoctor = buildDoctor(doctorCommand);
                            doctorService.validateAddingDoctor(createdDoctor);
                            return createdDoctor;
                        });
                doctors.add(doctor);
            });
        }
        doctors.forEach(doctor -> doctor.getInstitutions().add(institution));
        institution.getDoctors().addAll(doctors);
    }

    private Doctor buildDoctor(DoctorCommand doctorCommand) {
        return Doctor.builder()
                .firstName(doctorCommand.getFirstName())
                .lastName(doctorCommand.getLastName())
                .email(doctorCommand.getEmail())
                .password(doctorCommand.getPassword())
                .specialization(doctorCommand.getSpecialization())
                .institutions(new HashSet<>())
                .build();
    }

    private Institution prepareInstitution(FullInstitutionDTO fullInstitutionDTORequest) {
        return institutionRepository.findByName(fullInstitutionDTORequest.getName())
                .orElseGet(() -> {
                    Institution institution = buildInstitution(fullInstitutionDTORequest);
                    validateAddingInstitution(institution);
                    return institution;
                });
    }

    private Institution buildInstitution(FullInstitutionDTO fullInstitutionDTORequest) {
        return Institution.builder()
                .name(fullInstitutionDTORequest.getName())
                .city(fullInstitutionDTORequest.getCity())
                .zipCode(fullInstitutionDTORequest.getZipCode())
                .street(fullInstitutionDTORequest.getStreet())
                .buildingNumber(fullInstitutionDTORequest.getBuildingNumber())
                .doctors(new HashSet<>())
                .build();
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

    public void editInstitutionByName(String name, InstitutionCommand institutionCommand) {
        Institution institution = institutionRepository.findByName(name)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution with name: " + name + " do not exists"));
        Institution newInstitutionData = institutionMapper.toEntity(institutionCommand);
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
