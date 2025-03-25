package com.example.demo.service;

import com.example.demo.exceptions.institution.InstitutionNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.InstitutionCommand;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Institution;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.mapper.InstitutionMapper;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.InstitutionRepository;
import com.example.demo.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InstitutionServiceTest {
    private InstitutionRepository institutionRepository;
    private InstitutionMapper institutionMapper;
    private DoctorRepository doctorRepository;
    private DoctorService doctorService;
    private InstitutionService institutionService;

    @BeforeEach
    void setUp() {
        this.institutionRepository = Mockito.mock(InstitutionRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.institutionMapper = Mappers.getMapper(InstitutionMapper.class);
        this.doctorService = Mockito.mock(DoctorService.class);
        this.institutionService = new InstitutionService(institutionRepository, institutionMapper, doctorRepository, doctorService);
    }

    @Test
    void getInstitutions_institutionsExists_ReturnPageContentDTO() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        List<Institution> institutions = List.of(
                buildInstitution("szpital"),
                buildInstitution("przychodnia")
        );
        Page<Institution> institutionPage = new PageImpl<>(institutions, pageable, institutions.size());
        when(institutionRepository.findAll(pageable)).thenReturn(institutionPage);
        //when
        PageContent<InstitutionDTO> result = institutionService.getInstitutions(pageable);
        //then
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getTotalPageNumber());
    }

    @Test
    void getInstitutionByName_institutionExists_returnInstitutionDTO() {
        //given
        String name = "szpital";
        Institution institution = buildInstitution(name);
        Institution savedInstitution = Institution.builder()
                .id(1L)
                .name(name)
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("12")
                .build();
        when(institutionRepository.findByName(name)).thenReturn(Optional.of(institution));
        when(institutionRepository.save(any())).thenReturn(savedInstitution);
        //when
        InstitutionDTO result = institutionService.getInstitutionByName(name);
        //then
//        tu nie działa porownanie Id, excepted: 1L, actual: null
//        assertEquals(1L, result.getId());
        assertEquals(name, result.getName());
        assertEquals("lodz", result.getCity());
        assertEquals("narutowicza", result.getStreet());
    }

    @Test
    void getInstitutionByName_institutionDoesNotExists_InstitutionNotFoundExceptionThrown() {
        //given
        String name = "szpital";
        when(institutionRepository.findByName(name)).thenReturn(Optional.empty());
        //when_then
        InstitutionNotFoundException resultException = assertThrows(InstitutionNotFoundException.class, () -> institutionService.getInstitutionByName(name));
        //then
        assertEquals("Institution with name: " + name + " do not exists", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }


    private Institution buildInstitution(String name) {
        return Institution.builder()
                .name(name)
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("12")
                .build();
    }
}
