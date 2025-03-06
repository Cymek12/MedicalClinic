package com.example.demo.model;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDTO map (Institution institution);

    List<InstitutionDTO> map (List<Institution> institutions);
}
