package com.example.demo.model;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO map(Doctor doctor);

    List<DoctorDTO> map(List<Doctor> doctors);
}
