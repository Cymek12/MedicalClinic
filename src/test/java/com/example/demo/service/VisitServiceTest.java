package com.example.demo.service;

import com.example.demo.exceptions.doctor.DoctorNotFoundException;
import com.example.demo.exceptions.patient.PatientNotFoundException;
import com.example.demo.exceptions.visit.*;
import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.VisitMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    private VisitRepository visitRepository;
    private DoctorRepository doctorRepository;
    private VisitMapper visitMapper;
    private PatientRepository patientRepository;
    private VisitService visitService;

    @BeforeEach
    void setUp() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.visitService = new VisitService(visitRepository, doctorRepository, visitMapper, patientRepository);
    }

    @Test
    void createVisit_validationPassedDoctorExists_returnVisitDTO() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0))))
                .build();
        Doctor doctor = buildDoctor();
        Visit exceptedVisit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0))))
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any())).thenReturn(exceptedVisit);
        //when
        VisitDTO resultVisitDTO = visitService.createVisit(email, visitCommand);
        //then
        ArgumentCaptor<Visit> visitCaptor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(visitCaptor.capture());

        assertEquals(1L, resultVisitDTO.getId());
        assertEquals(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)), visitCaptor.getValue().getStartDateTime());
        assertEquals(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0)), visitCaptor.getValue().getEndDateTime());
        assertEquals(1L, visitCaptor.getValue().getDoctor().getId());
        assertEquals("test@gmail.com", visitCaptor.getValue().getDoctor().getEmail());
    }

    @Test
    void createVisit_doctorDoesNotExists_DoctorNotFoundExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0))))
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        DoctorNotFoundException resultException = assertThrows(DoctorNotFoundException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Doctor with email: " + email + " do not exists", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void createVisit_requestFieldIsNull_VisitDataIsNullExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(null)
                .endDateTime(null)
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when_then
        VisitDataIsNullException resultException = assertThrows(VisitDataIsNullException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Visit data cannot be null", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void createVisit_dateTimeFromPast_VisitDateTimeFromPastExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.of(2020, 3, 25), LocalTime.of(16, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.of(2025, 3, 25), LocalTime.of(18, 0))))
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when_then
        VisitDateTimeFromPastException resultException = assertThrows(VisitDateTimeFromPastException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Date and time visit cannot be from the past", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void createVisit_wrongTimeFormat_WrongVisitTimeFormatExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 12, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 15, 0))))
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when_then
        WrongVisitTimeFormatException resultException = assertThrows(WrongVisitTimeFormatException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Visit can only be scheduled at 15-minute intervals", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void createVisit_endTimeAfterStartTime_WrongVisitTimeFormatExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 15, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 30, 0))))
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when_then
        WrongVisitTimeFormatException resultException = assertThrows(WrongVisitTimeFormatException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Visit end date time should be after start date time", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void createVisit_overlappingVisit_VisitOverlapsExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 15, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 30, 0))))
                .build();
        Doctor doctor = buildDoctor();
        Visit visit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 15, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 30, 0))))
                .build();
        List<Visit> overlappingVisits = List.of(visit);
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(visitRepository.findOverlappingVisits(doctor, visitCommand.getStartDateTime(), visitCommand.getEndDateTime())).thenReturn(overlappingVisits);
        //when_then
        VisitOverlapsException resultException = assertThrows(VisitOverlapsException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Visit cannot overlaps with another visit", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void createVisit_violatingBreakVisit_VisitViolatesBreakExceptionThrown() {
        //given
        String email = "test@gmail.com";
        VisitCommand visitCommand = VisitCommand.builder()
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 15, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 30, 0))))
                .build();
        Doctor doctor = buildDoctor();
        Visit visit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 15, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 30, 0))))
                .build();
        List<Visit> violatingBreakVisits = List.of(visit);
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(visitRepository.findOverlappingVisits(doctor, visitCommand.getStartDateTime(), visitCommand.getEndDateTime())).thenReturn(new ArrayList<>());
        when(visitRepository.findVisitsViolatingBreak(doctor, visitCommand.getStartDateTime(), visitCommand.getEndDateTime())).thenReturn(violatingBreakVisits);
        //when_then
        VisitViolatesBreakException resultException = assertThrows(VisitViolatesBreakException.class, () -> visitService.createVisit(email, visitCommand));
        //then
        assertEquals("Visit should be separated by 15-minute break", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void reserveVisit_validationPassed_visitReserved() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        Patient patient = Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Visit visit = Visit.builder()
                .id(1L)
                .startDateTime(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16, 15, 0)))
                .endDateTime((LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 30, 0))))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(visit));
        //when
        visitService.reserveVisit(email, id);
        //then
        ArgumentCaptor<Visit> visitCaptor = ArgumentCaptor.forClass(Visit.class);
        verify(visitRepository).save(visitCaptor.capture());
        assertEquals(1L, visitCaptor.getValue().getId());
        assertEquals(1L, visitCaptor.getValue().getPatient().getId());
        assertEquals("test@gmail.com", visitCaptor.getValue().getPatient().getEmail());
        assertEquals("pass", visitCaptor.getValue().getPatient().getPassword());
        assertEquals("jan", visitCaptor.getValue().getPatient().getFirstName());
        assertEquals("kowalski", visitCaptor.getValue().getPatient().getLastName());
        assertEquals("123456789", visitCaptor.getValue().getPatient().getPhoneNumber());
    }

    @Test
    void reserveVisit_patientNotFound_PatientNotFoundExceptionThrown() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        PatientNotFoundException resultException = assertThrows(PatientNotFoundException.class, () -> visitService.reserveVisit(email, id));
        //then
        assertEquals("Patient with email: " + email + " does not exist", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void reserveVisit_visitNotFound_VisitNotFoundExceptionThrown() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        Patient patient = Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());
        //when_then
        VisitNotFoundException resultException = assertThrows(VisitNotFoundException.class, () -> visitService.reserveVisit(email, id));
        //then
        assertEquals("Visit with id: " + id + " does not exist", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    private Doctor buildDoctor() {
        return Doctor.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .firstName("jan")
                .lastName("kowalski")
                .specialization("Kardiolog")
                .institutions(new HashSet<>())
                .build();
    }
}
