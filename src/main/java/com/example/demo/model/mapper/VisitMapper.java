package com.example.demo.model.mapper;

import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mappings({
            @Mapping(source = "doctor", target = "doctorDTO"),
            @Mapping(source = "patient", target = "patientDTO")
    })
    VisitDTO toDTO(Visit visit);

    List<VisitDTO> toDTO(List<Visit> visits);

    Visit toEntity(VisitCommand visitCommand);
}
