package com.example.demo.service;

import com.example.demo.exceptions.patient.PatientDataIsNullException;
import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientServiceTest {
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;
    private PatientService patientService;

    @BeforeEach
    void setUp(){
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientService = new PatientService(patientRepository, patientMapper);
    }

    @Test
    void editPatient_PatientExists_DataChanged(){
        //given
        String email = "test@gmail.com";
        PatientCommand patientCommand = PatientCommand.builder()
                .email("newemail@gmail.com")
                .password("newpass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000, 2, 17))
                .build();
        Patient currentPatient = Patient.builder()
                .email(email)
                .password("pass")
                .idCardNo("123")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
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
    void getPatientByEmail_PatientExists_ReturnPatient() {
        //given
        String email = "test@gmail.com";
        Patient foundPatient = Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        PatientDTO expectedPatientDTO = PatientDTO.builder()
                .id(1L)
                .email("test@gmail.com")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(foundPatient));
        //when
        PatientDTO patientDTOResult = patientService.getPatientByEmail(email);
        //then
        assertEquals(expectedPatientDTO.getId(), patientDTOResult.getId());
        assertEquals(expectedPatientDTO.getEmail(), patientDTOResult.getEmail());
        assertEquals(expectedPatientDTO.getIdCardNo(), patientDTOResult.getIdCardNo());
        assertEquals(expectedPatientDTO.getFirstName(), patientDTOResult.getFirstName());
        assertEquals(expectedPatientDTO.getLastName(), patientDTOResult.getLastName());
        assertEquals(expectedPatientDTO.getPhoneNumber(), patientDTOResult.getPhoneNumber());
        assertEquals(expectedPatientDTO.getBirthday(), patientDTOResult.getBirthday());
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
                .birthday(LocalDate.of(2000,2,17))
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        PatientDTO expectedDTOPatient = PatientDTO.builder()
                .id(1L)
                .email("test@gmail.com")
                .idCardNo("456")
                .firstName("jan")
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        when(patientRepository.save(any())).thenReturn(patient);
        //when
        PatientDTO resultPatient = patientService.addPatient(patientCommand);
        //then
        assertEquals(expectedDTOPatient.getId(), resultPatient.getId());
        assertEquals(expectedDTOPatient.getEmail(), resultPatient.getEmail());
        assertEquals(expectedDTOPatient.getIdCardNo(), resultPatient.getIdCardNo());
        assertEquals(expectedDTOPatient.getFirstName(), resultPatient.getFirstName());
        assertEquals(expectedDTOPatient.getLastName(), resultPatient.getLastName());
        assertEquals(expectedDTOPatient.getPhoneNumber(), resultPatient.getPhoneNumber());
        assertEquals(expectedDTOPatient.getBirthday(), resultPatient.getBirthday());
    }

    @Test
    void addPatient_patientDataIsNull_exceptionThrown(){
        //given
        PatientCommand patientCommand = PatientCommand.builder()
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName(null)
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        Patient patientToSave = Patient.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .idCardNo("456")
                .firstName(null)
                .lastName("kowalski")
                .phoneNumber("123456789")
                .birthday(LocalDate.of(2000,2,17))
                .build();
        //when
        PatientDataIsNullException result = assertThrows(PatientDataIsNullException.class, () -> patientService.addPatient(patientCommand));
        //then
        assertEquals("Patient fields cannot be null", result.getMessage());
    }






}
