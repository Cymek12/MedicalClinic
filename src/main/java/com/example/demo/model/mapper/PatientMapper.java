package com.example.demo.model.mapper;

import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Patient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toDTO(Patient patient);

    List<PatientDTO> toDTO(List<Patient> patients);

    Patient toEntity(PatientCommand patientCommand);
}
