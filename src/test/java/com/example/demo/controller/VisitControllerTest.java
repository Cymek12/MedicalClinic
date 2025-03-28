package com.example.demo.controller;


import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @MockitoBean
    private VisitService visitService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createVisit_returnVisitDTO() throws Exception {
        VisitCommand visitCommand = buildDoctorCommand();
        DoctorDTO doctorDTO = buildDoctorDTO(1L, "doctor@gmail.com");
        VisitDTO visitDTO = buildVisitDTO(1L, doctorDTO, null);
        String doctorEmail = "new@gmail.com";
        when(visitService.createVisit(eq(doctorEmail), any())).thenReturn(visitDTO);

        mockMvc.perform(post("/visits/{doctorEmail}", doctorEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visitCommand)))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.doctorDTO.id").value(1L))
                .andExpect(jsonPath("$.doctorDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.doctorDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.doctorDTO.email").value("doctor@gmail.com"))
                .andExpect(jsonPath("$.doctorDTO.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.doctorDTO.institutionIds").isArray());
    }

    @Test
    void createVisit_returnHttpStatus() throws Exception {

    }

    private VisitDTO buildVisitDTO(Long id, DoctorDTO doctorDTO, PatientDTO patientDTO) {
        return VisitDTO.builder()
                .id(id)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0,0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0,0)))
                .doctorDTO(doctorDTO)
                .patientDTO(patientDTO)
                .build();
    }

    private VisitCommand buildDoctorCommand() {
        return VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0,0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0,0)))
                .build();
    }

    private DoctorDTO buildDoctorDTO(Long id, String email) {
        return DoctorDTO.builder()
                .id(id)
                .firstName("jan")
                .lastName("kowalski")
                .email(email)
                .specialization("kardiolog")
                .institutionIds(new ArrayList<>())
                .build();
    }

    private PatientDTO buildPatientDTO(Long id, String email) {
        return PatientDTO.builder()
                .id(id)
                .email(email)
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
    }
}
