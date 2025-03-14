package com.example.demo.model.mapper;

import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.model.entity.Institution;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDTO map(Institution institution);

    List<InstitutionDTO> map(List<Institution> institutions);
}
