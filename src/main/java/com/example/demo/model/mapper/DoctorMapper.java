package com.example.demo.model.mapper;

import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.entity.Doctor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDTO(Doctor doctor);

    List<DoctorDTO> toDTO(List<Doctor> doctors);

    Doctor toEntity(DoctorCommand doctorCommand);
}
