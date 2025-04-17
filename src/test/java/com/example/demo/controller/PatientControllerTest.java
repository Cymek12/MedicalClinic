package com.example.demo.controller;

import com.example.demo.exceptions.patient.PatientAlreadyExistException;
import com.example.demo.exceptions.patient.PatientNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.PasswordRequest;
import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.service.PatientService;
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

import static com.example.demo.TestDataBuilder.buildPatientCommand;
import static com.example.demo.TestDataBuilder.buildPatientDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class PatientControllerTest {
    @MockitoBean
    private PatientService patientService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPatients_returnPageContentDTO() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        List<PatientDTO> patients = List.of(
                buildPatientDTO(1L, "test@gmail.com"),
                buildPatientDTO(2L, "test2@gmail.com")
        );
        PageContent<PatientDTO> pageContentDTO = new PageContent<>(2L, 0, 1, patients);
        when(patientService.getPatients(pageable)).thenReturn(pageContentDTO);
        mockMvc.perform(get("/patients?size=5&page=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
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
                .andExpect(jsonPath("$.content[1].idCardNo").value("123"))
                .andExpect(jsonPath("$.content[1].firstName").value("jan"))
                .andExpect(jsonPath("$.content[1].lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[1].phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.content[1].birthday").value("2000-02-17"));
    }

    @Test
    void getPatientByEmail_returnPatientDTO() throws Exception {
        String email = "new@gmail.com";
        PatientDTO patientDTO = buildPatientDTO(1L, "new@gmail.com");
        when(patientService.getPatientByEmail(email)).thenReturn(patientDTO);
        mockMvc.perform(get("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(email))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"));
    }

    @Test
    void getPatientByEmail_returnHTTPStatus() throws Exception {
        String email = "new@gmail.com";
        when(patientService.getPatientByEmail(email)).thenThrow(new PatientNotFoundException("Patient not found"));
        mockMvc.perform(get("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(email))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void addPatient_returnPatientDTO() throws Exception {
        PatientCommand patientCommand = buildPatientCommand("new@gmail.com");
        PatientDTO patientDTO = buildPatientDTO(1L, "new@gmail.com");
        when(patientService.addPatient(any())).thenReturn(patientDTO);
        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"));
    }

    @Test
    void addPatient_returnHttpStatus() throws Exception {
        PatientCommand patientCommand = buildPatientCommand("new@gmail.com");
        when(patientService.addPatient(any())).thenThrow(new PatientAlreadyExistException("Patient already exist"));
        mockMvc.perform(post("/patients") //perform symuluje request
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Patient already exist"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"));
    }

    @Test
    void deletePatientByEmail() throws Exception {
        String email = "new@gmail.com";
        mockMvc.perform(delete("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deletePatientByEmail_returnHttpStatus() throws Exception {
        String email = "new@gmail.com";
        doThrow(new PatientNotFoundException("Patient not found")).when(patientService).deletePatientByEmail(email);
        mockMvc.perform(delete("/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void editPatient_returnPatientDTO() throws Exception {
        String email = "new@gmail.com";
        PatientCommand patientCommand = buildPatientCommand("new2@gmail.com");
        PatientDTO patientDTO = buildPatientDTO(1L, "new2@gmail.com");
        when(patientService.editPatient(eq(email), any())).thenReturn(patientDTO);
        mockMvc.perform(put("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new2@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"));
    }

    @Test
    void editPatient_returnHttpStatus() throws Exception {
        String email = "new@gmail.com";
        PatientCommand patientCommand = buildPatientCommand("new2@gmail.com");
        when(patientService.editPatient(eq(email), any())).thenThrow(new PatientNotFoundException("Patient not found"));
        mockMvc.perform(put("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(patientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void editPassword_returnPatientDTO() throws Exception {
        String email = "new@gmail.com";
        PasswordRequest passwordRequest = new PasswordRequest("newPass");
        PatientDTO patientDTO = buildPatientDTO(1L, "new@gmail.com");
        when(patientService.editPassword(eq(email), any())).thenReturn(patientDTO);
        mockMvc.perform(patch("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(passwordRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.idCardNo").value("123"))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.birthday").value("2000-02-17"));
    }

    @Test
    void editPassword_returnHttpStatus() throws Exception {
        String email = "new@gmail.com";
        PasswordRequest passwordRequest = new PasswordRequest("newPass");
        when(patientService.editPassword(eq(email), any())).thenThrow(new PatientNotFoundException("Patient not found"));
        mockMvc.perform(patch("/patients/{email}", email)
                        .content(objectMapper.writeValueAsString(passwordRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }
}