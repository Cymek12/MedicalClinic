package com.example.demo.controller;

import com.example.demo.exceptions.doctor.DoctorDataIsNullException;
import com.example.demo.exceptions.doctor.DoctorNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.service.DoctorService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {
    @MockitoBean
    private DoctorService doctorService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addDoctor_returnDoctorDTO() throws Exception {
        DoctorCommand doctorCommand = buildDoctorCommand("new@gmail.com");
        DoctorDTO doctorDTO = buildDoctorDTO(1L, "new@gmail.com");
        when(doctorService.addDoctor(any())).thenReturn(doctorDTO);
        mockMvc.perform(post("/doctors")
                .content(objectMapper.writeValueAsString(doctorCommand))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.institutionIds").isArray());
    }

    @Test
    void addDoctor_returnHttpStatus() throws Exception {
        DoctorCommand doctorCommand = buildDoctorCommand("new@gmail.com");
        when(doctorService.addDoctor(any())).thenThrow(new DoctorDataIsNullException("Request data cannot be null"));
        mockMvc.perform(post("/doctors")
                        .content(objectMapper.writeValueAsString(doctorCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Request data cannot be null"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"));
    }

    @Test
    void getDoctors_returnPageContentDTO() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        List<DoctorDTO> doctors = List.of(
                buildDoctorDTO(1L, "test@gmail.com"),
                buildDoctorDTO(2L, "test2@gmail.com")
        );
        PageContent<DoctorDTO> pageContentDTO = new PageContent<>(2L, 0, 1, doctors);
        when(doctorService.getDoctors(pageable)).thenReturn(pageContentDTO);
        mockMvc.perform(get("/doctors?size=5&page=0")//.param(size).param(page)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2L))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPageNumber").value(1))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("jan"))
                .andExpect(jsonPath("$.content[0].lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.content[0].specialization").value("kardiolog"))
                .andExpect(jsonPath("$.content[0].institutionIds").isArray())
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].firstName").value("jan"))
                .andExpect(jsonPath("$.content[1].lastName").value("kowalski"))
                .andExpect(jsonPath("$.content[1].email").value("test2@gmail.com"))
                .andExpect(jsonPath("$.content[1].specialization").value("kardiolog"))
                .andExpect(jsonPath("$.content[1].institutionIds").isArray());
    }

    @Test
    void getDoctorByEmail_returnDoctorDTO() throws Exception {
        String email = "new@gmail.com";
        DoctorDTO doctorDTO = buildDoctorDTO(1L, "new@gmail.com");
        when(doctorService.getDoctorByEmail(email)).thenReturn(doctorDTO);
        mockMvc.perform(get("/doctors/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.institutionIds").isArray());
    }

    @Test
    void getDoctorByEmail_returnHttpStatus() throws Exception {
        String email = "new@gmail.com";
        when(doctorService.getDoctorByEmail(email)).thenThrow(new DoctorNotFoundException("Doctor not found"));
        mockMvc.perform(get("/doctors/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void editDoctorByEmail_returnDoctorDTO() throws Exception {
        String email = "new@gmail.com";
        DoctorCommand doctorCommand = buildDoctorCommand("new2@gmail.com");
        DoctorDTO doctorDTO = buildDoctorDTO(1L, "new2@gmail.com");
        when(doctorService.editDoctorByEmail(eq(email), any())).thenReturn(doctorDTO);
        mockMvc.perform(put("/doctors/{email}", email)
                .content(objectMapper.writeValueAsString(doctorCommand))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.email").value("new2@gmail.com"))
                .andExpect(jsonPath("$.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.institutionIds").isArray());
    }

    @Test
    void editDoctorByEmail_returnHttpStatus() throws Exception {
        String email = "new@gmail.com";
        DoctorCommand doctorCommand = buildDoctorCommand("new2@gmail.com");
        when(doctorService.editDoctorByEmail(eq(email), any())).thenThrow(new DoctorNotFoundException("Doctor not found"));
        mockMvc.perform(put("/doctors/{email}", email)
                .content(objectMapper.writeValueAsString(doctorCommand))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void deleteDoctorByEmail_returnHttpStatus() throws Exception {
        String email = "new@gmail.com";
        doThrow(new DoctorNotFoundException("Doctor not found")).when(doctorService).deleteDoctorByEmail(email);
        mockMvc.perform(delete("/doctors/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
    }

    @Test
    void deleteDoctorByEmail() throws Exception {
        String email = "new@gmail.com";
        mockMvc.perform(delete("/doctors/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addInstitution_returnDoctorDTO() throws Exception {
        String doctorEmail = "new@gmail.com";
        String institutionId = "1";
        DoctorDTO doctorDTO = DoctorDTO.builder()
                .id(1L)
                .firstName("jan")
                .lastName("kowalski")
                .email("new@gmail.com")
                .specialization("kardiolog")
                .institutionIds(List.of(Long.valueOf(institutionId)))
                .build();
        when(doctorService.addInstitution(eq(doctorEmail), eq(institutionId))).thenReturn(doctorDTO);
        mockMvc.perform(patch("/doctors/institution/{doctorEmail}/{institutionId}", doctorEmail, institutionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("jan"))
                .andExpect(jsonPath("$.lastName").value("kowalski"))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.specialization").value("kardiolog"))
                .andExpect(jsonPath("$.institutionIds").isArray())
                .andExpect(jsonPath("$.institutionIds[0]").value(1L));
    }

    @Test
    void addInstitution_returnHttpStatus() throws Exception {
        String doctorEmail = "new@gmail.com";
        String institutionId = "1";
        when(doctorService.addInstitution(eq(doctorEmail), eq(institutionId))).thenThrow(new DoctorNotFoundException("Doctor not found"));
        mockMvc.perform(patch("/doctors/institution/{doctorEmail}/{institutionId}", doctorEmail, institutionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"));
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

    private DoctorCommand buildDoctorCommand(String email) {
        return DoctorCommand.builder()
                .firstName("jan")
                .lastName("kowalski")
                .email(email)
                .password("pass")
                .specialization("kardiolog")
                .build();
    }
}
