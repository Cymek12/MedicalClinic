package com.example.demo.service;

import com.example.demo.exceptions.*;
import com.example.demo.model.InstitutionMapper;
import com.example.demo.model.Institution;
import com.example.demo.model.InstitutionDTO;
import com.example.demo.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;

    public List<InstitutionDTO> getInstitutions() {
        return institutionMapper.map(institutionRepository.findAll());
    }

    public InstitutionDTO getInstitutionByName(String name) {
        return institutionMapper.map(institutionRepository.findByName(name)
                .orElseThrow(() -> new InstitutionNotFoundException("Institution with name: " + name + " do not exists")));
    }

    public void addInstitution(Institution institution) {
        validateAddingInstitution(institution);
        institutionRepository.save(institution);
    }

    private void validateAddingInstitution(Institution institution) {
        if (isInstitutionDataNull(institution)) {
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
        editInstitutionData(institution, newInstitutionData);
        institutionRepository.save(institution);
    }

    private void validateNewInstitutionData(Institution institution, Institution newInstitutionData) {
        if (isInstitutionDataNull(institution)) {
            throw new InstitutionDataIsNullException("Institution fields cannot be null");
        }
        if (institutionRepository.existsByName(institution.getName()) && !institution.getName().equals(newInstitutionData.getName())) {
            throw new InstitutionNotFoundException("Institution with name: " + newInstitutionData.getName() + " do not exists");
        }
    }

    private void editInstitutionData(Institution institution, Institution newInstitutionData) {
        institution.setName(newInstitutionData.getName());
        institution.setCity(newInstitutionData.getCity());
        institution.setZipCode(newInstitutionData.getZipCode());
        institution.setStreet(newInstitutionData.getStreet());
        institution.setBuildingNumber(newInstitutionData.getBuildingNumber());
    }

    private boolean isInstitutionDataNull(Institution institution) {
        return institution.getName() == null ||
                institution.getCity() == null ||
                institution.getZipCode() == null ||
                institution.getStreet() == null ||
                institution.getBuildingNumber() == null;
    }
}
