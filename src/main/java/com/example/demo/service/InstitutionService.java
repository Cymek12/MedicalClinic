package com.example.demo.service;

import com.example.demo.model.Institution;
import com.example.demo.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    public void addInstitution(Institution institution){
        institutionRepository.save(institution);
    }
}
