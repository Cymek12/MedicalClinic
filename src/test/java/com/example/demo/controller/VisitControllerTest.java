package com.example.demo.controller;


import com.example.demo.exceptions.doctor.DoctorNotFoundException;
import com.example.demo.exceptions.visit.VisitNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.ReserveVisitCommand;
import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.service.VisitService;
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

import java.util.List;

import static com.example.demo.TestDataBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
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
        VisitCommand visitCommand = buildDoctorCommand();
        String doctorEmail = "new@gmail.com";
        when(visitService.createVisit(eq(doctorEmail), any())).thenThrow(new DoctorNotFoundException("Doctor do not exists"));
        mockMvc.perform(post("/visits/{doctorEmail}", doctorEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitCommand)))
                .andDo(print());
    }

    @Test
    void getAvailableVisits_returnPageContentDTO() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        List<VisitDTO> visits = List.of(
                buildVisitDTO(1L, buildDoctorDTO(1L, "doctor@gmail.com"), buildPatientDTO(1L, "patient@gmail.com")),
                buildVisitDTO(2L, buildDoctorDTO(2L, "doctor2@gmail.com"), buildPatientDTO(2L, "patient2@gmail.com"))
        );
        PageContent<VisitDTO> pageContentDTO = new PageContent<>(2L, 0, 1, visits);
        when(visitService.getAvailableVisitsProcessor(pageable, null, null, null, null)).thenReturn(pageContentDTO);
        mockMvc.perform(get("/visits/available")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2L))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPageNumber").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].doctorDTO.id").value(1L))
                .andExpect(jsonPath("$.content[0].doctorDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[0].doctorDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[0].doctorDTO.email").value("doctor@gmail.com"))
                .andExpect(jsonPath("$.content[0].doctorDTO.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.content[0].doctorDTO.institutionIds").isArray())
                .andExpect(jsonPath("$.content[0].patientDTO.id").value(1L))
                .andExpect(jsonPath("$.content[0].patientDTO.email").value("patient@gmail.com"))
                .andExpect(jsonPath("$.content[0].patientDTO.idCardNo").value("123"))
                .andExpect(jsonPath("$.content[0].patientDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[0].patientDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[0].patientDTO.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.content[0].patientDTO.birthday").value("2000-02-17"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].doctorDTO.id").value(2L))
                .andExpect(jsonPath("$.content[1].doctorDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[1].doctorDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[1].doctorDTO.email").value("doctor2@gmail.com"))
                .andExpect(jsonPath("$.content[1].doctorDTO.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.content[1].doctorDTO.institutionIds").isArray())
                .andExpect(jsonPath("$.content[1].patientDTO.id").value(2L))
                .andExpect(jsonPath("$.content[1].patientDTO.email").value("patient2@gmail.com"))
                .andExpect(jsonPath("$.content[1].patientDTO.idCardNo").value("123"))
                .andExpect(jsonPath("$.content[1].patientDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[1].patientDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[1].patientDTO.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.content[1].patientDTO.birthday").value("2000-02-17"));
    }

    @Test
    void reserveVisit_returnVisitDTO() throws Exception {
        VisitDTO visitDTO = buildVisitDTO(1L, null, null);
         ReserveVisitCommand reserveVisitCommand = ReserveVisitCommand.builder()
                .patientEmail("patient@gmail.com")
                .visitId("1")
                 .build();
         String patientEmail = "patient@gmail.com";
         String visitId = "1";
         when(visitService.reserveVisit(eq(patientEmail), eq(visitId))).thenReturn(visitDTO);
        mockMvc.perform(patch("/visits/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserveVisitCommand)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void reserveVisit_returnHttpStatus() throws Exception {
        ReserveVisitCommand reserveVisitCommand = ReserveVisitCommand.builder()
                .patientEmail("patient@gmail.com")
                .visitId("1")
                .build();
        String patientEmail = "patient@gmail.com";
        String visitId = "1";
        doThrow(new VisitNotFoundException("Visit does not exist")).when(visitService).reserveVisit(patientEmail, visitId);
        mockMvc.perform(patch("/visits/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserveVisitCommand)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Visit does not exist"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void getVisits_returnPageContentDTO() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        List<VisitDTO> visits = List.of(
                buildVisitDTO(1L, buildDoctorDTO(1L, "doctor@gmail.com"), buildPatientDTO(1L, "patient@gmail.com")),
                buildVisitDTO(2L, buildDoctorDTO(2L, "doctor2@gmail.com"), buildPatientDTO(2L, "patient2@gmail.com"))
        );
        PageContent<VisitDTO> pageContentDTO = new PageContent<>(2L, 0, 1, visits);
        when(visitService.getVisits(pageable)).thenReturn(pageContentDTO);
        mockMvc.perform(get("/visits?size=5&page=0")//.param(size).param(page)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2L))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPageNumber").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].doctorDTO.id").value(1L))
                .andExpect(jsonPath("$.content[0].doctorDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[0].doctorDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[0].doctorDTO.email").value("doctor@gmail.com"))
                .andExpect(jsonPath("$.content[0].doctorDTO.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.content[0].doctorDTO.institutionIds").isArray())
                .andExpect(jsonPath("$.content[0].patientDTO.id").value(1L))
                .andExpect(jsonPath("$.content[0].patientDTO.email").value("patient@gmail.com"))
                .andExpect(jsonPath("$.content[0].patientDTO.idCardNo").value("123"))
                .andExpect(jsonPath("$.content[0].patientDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[0].patientDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[0].patientDTO.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.content[0].patientDTO.birthday").value("2000-02-17"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].doctorDTO.id").value(2L))
                .andExpect(jsonPath("$.content[1].doctorDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[1].doctorDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[1].doctorDTO.email").value("doctor2@gmail.com"))
                .andExpect(jsonPath("$.content[1].doctorDTO.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.content[1].doctorDTO.institutionIds").isArray())
                .andExpect(jsonPath("$.content[1].patientDTO.id").value(2L))
                .andExpect(jsonPath("$.content[1].patientDTO.email").value("patient2@gmail.com"))
                .andExpect(jsonPath("$.content[1].patientDTO.idCardNo").value("123"))
                .andExpect(jsonPath("$.content[1].patientDTO.firstName").value("jan"))
                .andExpect(jsonPath("$.content[1].patientDTO.lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[1].patientDTO.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.content[1].patientDTO.birthday").value("2000-02-17"));
    }
}