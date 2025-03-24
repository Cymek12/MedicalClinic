package com.example.demo.service;

import com.example.demo.exceptions.patient.CannotChangeIdCardNoException;
import com.example.demo.exceptions.patient.PatientAlreadyExistException;
import com.example.demo.exceptions.patient.PatientDataIsNullException;
import com.example.demo.exceptions.patient.PatientNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.PasswordRequest;
import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.mapper.PatientMapper;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientService = new PatientService(patientRepository, patientMapper);
    }

    @Test
    void getPatients_patientsExists_ReturnPageContentDTO() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        Patient currentPatient = buildPatient();
        List<Patient> patients = new ArrayList<>();
        patients.add(currentPatient);
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, patients.size());
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);
        //when
        PageContent<PatientDTO> result = patientService.getPatients(pageable);
        //then
        assertEquals(1, result.getTotalElements());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getTotalPageNumber());
        assertEquals(1L, result.getContent().getFirst().getId());
        assertEquals("test@gmail.com", result.getContent().getFirst().getEmail());
        assertEquals("123", result.getContent().getFirst().getIdCardNo());
        assertEquals("jan", result.getContent().getFirst().getFirstName());
        assertEquals("kowalski", result.getContent().getFirst().getLastName());
        assertEquals("123456789", result.getContent().getFirst().getPhoneNumber());
        assertEquals(LocalDate.of(2000, 2, 17), result.getContent().getFirst().getBirthday());
    }

    @Test
    void getPatientByEmail_PatientExists_ReturnPatientDTO() {
        //given
        String email = "test@gmail.com";
        Patient foundPatient = buildPatient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(foundPatient));
        //when
        PatientDTO patientDTOResult = patientService.getPatientByEmail(email);
        //then
        assertEquals(1L, patientDTOResult.getId());
        assertEquals("test@gmail.com", patientDTOResult.getEmail());
        assertEquals("123", patientDTOResult.getIdCardNo());
        assertEquals("jan", patientDTOResult.getFirstName());
        assertEquals("kowalski", patientDTOResult.getLastName());
        assertEquals("123456789", patientDTOResult.getPhoneNumber());
        assertEquals(LocalDate.of(2000, 2, 17), patientDTOResult.getBirthday());
    }

    @Test
    void getPatientByEmail_patientDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        //when_then
        PatientNotFoundException resultException = assertThrows(PatientNotFoundException.class, () -> patientService.getPatientByEmail(email));
        //then
        assertEquals("Patient with email: " + email + " does not exist", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void addPatient_validationPassed_PatientAdded() {
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Patient patientToAdd = buildPatient();
        when(patientRepository.save(any())).thenReturn(patientToAdd);
        //when
        PatientDTO resultPatient = patientService.addPatient(patientCommand);
        //then
        assertEquals(1L, resultPatient.getId());
        assertEquals("test@gmail.com", resultPatient.getEmail());
        assertEquals("123", resultPatient.getIdCardNo());
        assertEquals("jan", resultPatient.getFirstName());
        assertEquals("kowalski", resultPatient.getLastName());
        assertEquals("123456789", resultPatient.getPhoneNumber());
        assertEquals(LocalDate.of(2000, 2, 17), resultPatient.getBirthday());
    }

    @Test
    void addPatient_patientDataIsNull_exceptionThrown() {
        //given
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName(null)
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        //when_then
        PatientDataIsNullException result = assertThrows(PatientDataIsNullException.class, () -> patientService.addPatient(patientCommand));
        //then
        assertEquals("Patient fields cannot be null", result.getMessage());
    }

    @Test
    void addPatient_patientAlreadyExists_exceptionThrown() {
        //given
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        when(patientRepository.existsByEmail(patientCommand.getEmail())).thenReturn(true);
        //when_then
        PatientAlreadyExistException resultException = assertThrows(PatientAlreadyExistException.class, () -> patientService.addPatient(patientCommand));
        //then
        assertEquals("Patient with email: test@gmail.com already exist", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void deletePatientByEmail_patientExists_deletePatient() {
        //given
        String email = "test@gmail.com";
        Patient foundPatient = buildPatient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(foundPatient));
        //when
        patientService.deletePatientByEmail(email);
        //then
        verify(patientRepository, times(1)).delete(foundPatient);
    }

    @Test
    void deletePatientByEmail_patientDoesNotExist_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        PatientNotFoundException resultException = assertThrows(PatientNotFoundException.class, () -> patientService.deletePatientByEmail(email));
        //then
        assertEquals("Patient with email: " + email + " does not exist", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void editPatient_validationPassed_DataChanged() {
        //given
        String email = "test@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("newemail@gmail.com")
                .password("newPass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Patient currentPatient = buildPatient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(currentPatient));
        when(patientRepository.save(any())).thenReturn(currentPatient);
        //when
        PatientDTO result = patientService.editPatient(email, patientCommand);
        //then
        assertEquals("newemail@gmail.com", result.getEmail());
        assertEquals("123", result.getIdCardNo());
        assertEquals("jan", result.getFirstName());
        assertEquals("kowalski", result.getLastName());
        assertEquals("123456789", result.getPhoneNumber());
        assertEquals(LocalDate.of(2000, 2, 17), result.getBirthday());
    }

    @Test
    void editPatient_patientDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("newPass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        PatientNotFoundException resultException = assertThrows(PatientNotFoundException.class, () -> patientService.editPatient(email, patientCommand));
        //then
        assertEquals("Patient with email: " + email + " does not exist", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void editPatient_changeIdCardNo_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("newPass")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Patient currentPatient = buildPatient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(currentPatient));
        //when_then
        CannotChangeIdCardNoException resultException = assertThrows(CannotChangeIdCardNoException.class, () -> patientService.editPatient(email, patientCommand));
        //then
        assertEquals("Id card number is unchangeable", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void editPatient_patientDataIsNull_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName(null)
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Patient currentPatient = buildPatient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(currentPatient));
        //when_then
        PatientDataIsNullException resultException = assertThrows(PatientDataIsNullException.class, () -> patientService.editPatient(email, patientCommand));
        //then
        assertEquals("Patient fields cannot be null", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void editPatient_newEmailIsReserved_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("new@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Patient patientToEdit = buildPatient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patientToEdit));
        when(patientRepository.existsByEmail(patientToEdit.getEmail())).thenReturn(true);
        //when_then
        PatientAlreadyExistException resultException = assertThrows(PatientAlreadyExistException.class, () -> patientService.editPatient(email, patientCommand));
        //then
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
        assertEquals("Patient with email: new@gmail.com already exist", resultException.getMessage());
    }

    @Test
    void editPassword_patientExists_returnPatientDTO() {
        //given
        String email = "test@gmail.com";
        PasswordRequest passwordRequest = new PasswordRequest("newPass");
        Patient currentPatient = buildPatient();
        Patient updatedPatient = Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("newPass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(currentPatient));
        when(patientRepository.save(any())).thenReturn(updatedPatient);
        //when
        PatientDTO result = patientService.editPassword(email, passwordRequest);
        //then
        assertEquals(1L, result.getId());
        assertEquals("test@gmail.com", result.getEmail());
    }

    @Test
    void editPassword_patientDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        PasswordRequest passwordRequest = new PasswordRequest("newPass");
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        PatientNotFoundException resultException = assertThrows(PatientNotFoundException.class, () -> patientService.editPassword(email, passwordRequest));
        //then
        assertEquals("Patient with email: " + email + " does not exist", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    private static Patient buildPatient() {
        return Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
    }
}
