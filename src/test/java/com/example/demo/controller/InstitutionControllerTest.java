package com.example.demo.controller;

import com.example.demo.exceptions.doctor.DoctorNotFoundException;
import com.example.demo.exceptions.institution.InstitutionAlreadyExistException;
import com.example.demo.exceptions.institution.InstitutionDataIsNullException;
import com.example.demo.exceptions.institution.InstitutionNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.command.InstitutionCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.FullInstitutionDTO;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.service.InstitutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class InstitutionControllerTest {
    @MockitoBean
    private InstitutionService institutionService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addInstitution_returnInstitutionDTO() throws Exception {
        InstitutionCommand institutionCommand = buildInstitutionCommand("szpital");
        InstitutionDTO institutionDTO = buildInstitutionDTO(1L, "szpital");
        when(institutionService.addInstitution(any())).thenReturn(institutionDTO);
        mockMvc.perform(post("/institutions")
                        .content(objectMapper.writeValueAsString(institutionCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("szpital"))
                .andExpect(jsonPath("$.city").value("lodz"))
                .andExpect(jsonPath("$.zipCode").value("12123"))
                .andExpect(jsonPath("$.street").value("narutowicza"))
                .andExpect(jsonPath("$.buildingNumber").value("1"))
                .andExpect(jsonPath("$.doctorIds").isArray());
    }

    @Test
    void addInstitution_returnHttpStatus() throws Exception {
        InstitutionCommand institutionCommand = buildInstitutionCommand("szpital");
        when(institutionService.addInstitution(any())).thenThrow(new InstitutionDataIsNullException("Request fields cannot be null"));
        mockMvc.perform(post("/institutions")
                        .content(objectMapper.writeValueAsString(institutionCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Request fields cannot be null"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"));
    }

    @Test
    void getInstitutions() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        List<InstitutionDTO> institutions = List.of(
                buildInstitutionDTO(1L, "szpital"),
                buildInstitutionDTO(2L, "przychodnia")
        );
        PageContent<InstitutionDTO> pageContentDTO = new PageContent<>(2L, 0, 1, institutions);
        when(institutionService.getInstitutions(pageable)).thenReturn(pageContentDTO);
        mockMvc.perform(get("/institutions?size=5&page=0")//.param(size).param(page)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2L))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPageNumber").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("szpital"))
                .andExpect(jsonPath("$.content[0].city").value("lodz"))
                .andExpect(jsonPath("$.content[0].zipCode").value("12123"))
                .andExpect(jsonPath("$.content[0].street").value("narutowicza"))
                .andExpect(jsonPath("$.content[0].buildingNumber").value("1"))
                .andExpect(jsonPath("$.content[0].doctorIds").isArray())
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("przychodnia"))
                .andExpect(jsonPath("$.content[1].city").value("lodz"))
                .andExpect(jsonPath("$.content[1].zipCode").value("12123"))
                .andExpect(jsonPath("$.content[1].street").value("narutowicza"))
                .andExpect(jsonPath("$.content[1].buildingNumber").value("1"))
                .andExpect(jsonPath("$.content[1].doctorIds").isArray());
    }

    @Test
    void getInstitutionByName_returnInstitutionDTO() throws Exception {
        String name = "szpital";
        InstitutionDTO institutionDTO = buildInstitutionDTO(1L, "szpital");
        when(institutionService.getInstitutionByName(name)).thenReturn(institutionDTO);
        mockMvc.perform(get("/institutions/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("szpital"))
                .andExpect(jsonPath("$.city").value("lodz"))
                .andExpect(jsonPath("$.zipCode").value("12123"))
                .andExpect(jsonPath("$.street").value("narutowicza"))
                .andExpect(jsonPath("$.buildingNumber").value("1"))
                .andExpect(jsonPath("$.doctorIds").isArray());
    }

    @Test
    void getInstitutionByName_returnHttpStatus() throws Exception {
        String name = "szpital";
        when(institutionService.getInstitutionByName(name)).thenThrow(new InstitutionNotFoundException("Institution not found"));
        mockMvc.perform(get("/institutions/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Institution not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void editInstitutionByName_returnInstitutionDTO() throws Exception {
        String name = "szpital";
        InstitutionCommand institutionCommand = buildInstitutionCommand("przychodnia");
        InstitutionDTO institutionDTO = buildInstitutionDTO(1L, "przychodnia");
        when(institutionService.editInstitutionByName(eq(name), any())).thenReturn(institutionDTO);
        mockMvc.perform(put("/institutions/{name}", name)
                        .content(objectMapper.writeValueAsString(institutionCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("przychodnia"))
                .andExpect(jsonPath("$.city").value("lodz"))
                .andExpect(jsonPath("$.zipCode").value("12123"))
                .andExpect(jsonPath("$.street").value("narutowicza"))
                .andExpect(jsonPath("$.buildingNumber").value("1"))
                .andExpect(jsonPath("$.doctorIds").isArray());
    }

    @Test
    void editInstitutionByName_returnHttpStatus() throws Exception {
        String name = "szpital";
        InstitutionCommand institutionCommand = buildInstitutionCommand("przychodnia");
        when(institutionService.editInstitutionByName(eq(name), any())).thenThrow(new InstitutionNotFoundException("Institution not found"));
        mockMvc.perform(put("/institutions/{name}", name)
                        .content(objectMapper.writeValueAsString(institutionCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Institution not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void deleteInstitutionByName() throws Exception {
        String name = "szpital";
        mockMvc.perform(delete("/institutions/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteInstitutionByName_returnHttpStatus() throws Exception {
        String name = "szpital";
        doThrow(new InstitutionNotFoundException("Institution not found")).when(institutionService).deleteInstitutionByName(name);
        mockMvc.perform(delete("/institutions/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Institution not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void addInstitutionsWithDoctors() throws Exception {
        List<FullInstitutionDTO> fullInstitutionDTOs = List.of(
                new FullInstitutionDTO("szpital", "lodz", "12123", "narutowicza", "1", Collections.singleton(buildDoctorCommand("new@gmail.com"))),
                new FullInstitutionDTO("przychodnia", "lodz", "12123", "narutowicza", "1", Collections.singleton(buildDoctorCommand("new2@gmail.com")))
        );
        mockMvc.perform(post("/institutions/bulk")
                        .content(objectMapper.writeValueAsString(fullInstitutionDTOs))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addInstitutionsWithDoctors_returnHttpStatus() throws Exception {
        List<FullInstitutionDTO> fullInstitutionDTOs = List.of(
                new FullInstitutionDTO("szpital", "lodz", "12123", "narutowicza", "1", Collections.singleton(buildDoctorCommand("new@gmail.com"))),
                new FullInstitutionDTO("przychodnia", "lodz", "12123", "narutowicza", "1", Collections.singleton(buildDoctorCommand("new2@gmail.com")))
        );
        doThrow(new InstitutionAlreadyExistException("Institution already exists")).when(institutionService).addInstitutionsWithDoctors(any());
        mockMvc.perform(post("/institutions/bulk")
                        .content(objectMapper.writeValueAsString(fullInstitutionDTOs))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Institution already exists"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"));
    }

    private DoctorCommand buildDoctorCommand(String email) {
        return DoctorCommand.builder()
                .firstName("jan")
                .lastName("kowalski")
                .email(email)
                .password("pass")
                .specialization("kardiolog")
                .build();
    }

    private InstitutionDTO buildInstitutionDTO(Long id, String name) {
        return InstitutionDTO.builder()
                .id(id)
                .name(name)
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("1")
                .doctorIds(new ArrayList<>())
                .build();
    }

    private InstitutionCommand buildInstitutionCommand(String name) {
        return InstitutionCommand.builder()
                .name(name)
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("1")
                .build();
    }
}
