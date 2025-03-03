package com.example.demo.model;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO map(Patient patient);

    List<PatientDTO> map(List<Patient> patients);
}
