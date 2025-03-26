package com.example.demo.controller;

import com.example.demo.exceptions.patient.PatientAlreadyExistException;
import com.example.demo.exceptions.patient.PatientDataIsNullException;
import com.example.demo.exceptions.patient.PatientNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.service.PatientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @MockitoBean
    private PatientService patientService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPatients_patientsExists_returnPageContentDTOJson() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        List<PatientDTO> patients = List.of(
                new PatientDTO(1L, "test@gmail.com", "123", "jan", "kowalski", "123456789", LocalDate.of(2000, 2, 17)),
                new PatientDTO(2L, "test2@gmail.com", "321", "krzysztof", "nowak", "987654321", LocalDate.of(2001, 3, 2))
        );
        PageContent<PatientDTO> pageContentDTO = new PageContent<>(2L, 0, 1, patients);
        when(patientService.getPatients(pageable)).thenReturn(pageContentDTO);
        //when_then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients?size=5&page=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2L))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPageNumber").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.content[0].idCardNo").value("123"))
                .andExpect(jsonPath("$.content[0].firstName").value("jan"))
                .andExpect(jsonPath("$.content[0].lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.content[0].birthday").value("2000-02-17"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].email").value("test2@gmail.com"))
                .andExpect(jsonPath("$.content[1].idCardNo").value("321"))
                .andExpect(jsonPath("$.content[1].firstName").value("krzysztof"))
                .andExpect(jsonPath("$.content[1].lastName").value("nowak"))
                .andExpect(jsonPath("$.content[1].phoneNumber").value("987654321"))
                .andExpect(jsonPath("$.content[1].birthday").value("2001-03-02"))
                .andDo(print());
    }

    @Test
    void getPatientByEmail_patientFound_returnJson() throws Exception {
        //given
        String email = "new@gmail.com";
        PatientDTO patientDTO = PatientDTO.builder()
                .id(1L)
                .email("new@gmail.com")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        when(patientService.getPatientByEmail(email)).thenReturn(patientDTO);
        //when_then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{email}", email)
                .content(objectMapper.writeValueAsString(email))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"))
                .andDo(print());
    }

    @Test
    void addPatient_patientAdded_returnJson() throws Exception {
        //given
        PatientCommand patientCommand = PatientCommand.builder()
                .email("new@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        PatientDTO patientDTO = PatientDTO.builder()
                .id(1L)
                .email("new@gmail.com")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        when(patientService.addPatient(any())).thenReturn(patientDTO);
        //when_then
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"))
                .andDo(print());
    }

    @Test
    void addPatient_patientAlreadyExists_returnExceptionJson() throws Exception {
        //given
        PatientCommand patientCommand = PatientCommand.builder()
                .email("new@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        when(patientService.addPatient(any())).thenThrow(new PatientAlreadyExistException("Patient with email: new@gmail.com already exist"));
        //when_then
        mockMvc.perform(MockMvcRequestBuilders.post("/patients") //perform symuluje request
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patient with email: new@gmail.com already exist"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andDo(print());
    }

    @Test
    void editPatient_patientExists_returnPatientDTO() throws Exception {
        //given
        String email = "new@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("new@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        PatientDTO patientDTO = PatientDTO.builder()
                .id(1L)
                .email("new@gmail.com")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        when(patientService.editPatient(eq(email), any())).thenReturn(patientDTO);
        //when_then
        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"))
                .andDo(print());
    }

    @Test
    void deletePatientByEmail_patientDoesNotExists_PatientNotFoundExceptionThrown() throws Exception {
        //given
        String email = "new@gmail.com";
        doThrow(new PatientNotFoundException("Patient not found")).when(patientService).deletePatientByEmail(email);
        //when_then
        mockMvc.perform(MockMvcRequestBuilders.delete("/patient/{email}", email)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }
}
