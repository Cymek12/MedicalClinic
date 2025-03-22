package com.example.demo.model.mapper;

import com.example.demo.model.command.InstitutionCommand;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.model.entity.Institution;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDTO toDTO(Institution institution);

    List<InstitutionDTO> toDTO(List<Institution> institutions);

    Institution toEntity(InstitutionCommand institutionCommand);
}
