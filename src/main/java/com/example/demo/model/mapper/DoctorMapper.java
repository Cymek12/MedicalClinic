package com.example.demo.model.mapper;

import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.entity.Doctor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO map(Doctor doctor);

    List<DoctorDTO> map(List<Doctor> doctors);
}
