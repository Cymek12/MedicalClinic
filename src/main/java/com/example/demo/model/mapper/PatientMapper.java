package com.example.demo.model.mapper;

import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Patient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO map(Patient patient);

    List<PatientDTO> map(List<Patient> patients);
}
