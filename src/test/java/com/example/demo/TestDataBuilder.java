package com.example.demo;

import com.example.demo.model.command.*;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Institution;
import com.example.demo.model.entity.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

public class TestDataBuilder {

    public static DoctorDTO buildDoctorDTO(Long id, String email) {
        return DoctorDTO.builder()
                .id(id)
                .firstName("jan")
                .lastName("kowalski")
                .email(email)
                .specialization("kardiolog")
                .institutionIds(new ArrayList<>())
                .build();
    }

    public static DoctorCommand buildDoctorCommand(String email) {
        return DoctorCommand.builder()
                .firstName("jan")
                .lastName("kowalski")
                .email(email)
                .password("pass")
                .specialization("kardiolog")
                .build();
    }

    public static AddInstitutionCommand buildAddInstitutionCommand() {
        return AddInstitutionCommand.builder()
                .doctorEmail("new@gmail.com")
                .institutionId("1")
                .build();
    }

    public static InstitutionDTO buildInstitutionDTO(Long id, String name) {
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

    public static InstitutionCommand buildInstitutionCommand(String name) {
        return InstitutionCommand.builder()
                .name(name)
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("1")
                .build();
    }


    public static PatientCommand buildPatientCommand(String email) {
        return PatientCommand.builder()
                .email(email)
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
    }

    public static PatientDTO buildPatientDTO(Long id, String email) {
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

    public static VisitDTO buildVisitDTO(Long id, DoctorDTO doctorDTO, PatientDTO patientDTO) {
        return VisitDTO.builder()
                .id(id)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0, 0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0, 0)))
                .doctorDTO(doctorDTO)
                .patientDTO(patientDTO)
                .build();
    }

    public static VisitCommand buildDoctorCommand() {
        return VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0, 0)))
                .endDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0, 0)))
                .build();
    }


    public static Doctor buildDoctor(String doctorEmail) {
        return Doctor.builder()
                .id(1L)
                .email(doctorEmail)
                .password("pass")
                .firstName("jan")
                .lastName("kowalski")
                .specialization("Kardiolog")
                .institutions(new HashSet<>())
                .build();
    }

    public static Institution buildInstitution(String name) {
        return Institution.builder()
                .id(1L)
                .name(name)
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("12")
                .build();
    }

    public static Patient buildPatient(String idCardNo) {
        return Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo(idCardNo)
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
    }
}