package com.example.demo.service;

import com.example.demo.exceptions.doctor.DoctorAlreadyExistException;
import com.example.demo.exceptions.doctor.DoctorDataIsNullException;
import com.example.demo.exceptions.doctor.DoctorNotFoundException;
import com.example.demo.exceptions.institution.InstitutionNotFoundException;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Institution;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import javax.print.Doc;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {
    private DoctorRepository doctorRepository;
    private DoctorMapper doctorMapper;
    private InstitutionRepository institutionRepository;
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.institutionRepository = Mockito.mock(InstitutionRepository.class);
        this.doctorService = new DoctorService(doctorRepository, doctorMapper, institutionRepository);
    }

    @Test
    void getDoctors_doctorsExists_returnPageContentDTO() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        Doctor doctor = buildDoctor();
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);
        //when
        PageContent<DoctorDTO> result = doctorService.getDoctors(pageable);
        //then
        assertEquals(1L, result.getTotalElements());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getTotalPageNumber());
        assertEquals(1L, result.getContent().getFirst().getId());
        assertEquals("test@gmail.com", result.getContent().getFirst().getEmail());
        assertEquals("jan", result.getContent().getFirst().getFirstName());
        assertEquals("kowalski", result.getContent().getFirst().getLastName());
        assertEquals("Kardiolog", result.getContent().getFirst().getSpecialization());
    }

    @Test
    void getDoctorByEmail_doctorExists_returnDoctorDTO() {
        //given
        String email = "test@gmail.com";
        Doctor foundDoctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(foundDoctor));
        //when
        DoctorDTO result = doctorService.getDoctorByEmail(email);
        //then
        assertEquals(1L, result.getId());
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("jan", result.getFirstName());
        assertEquals("kowalski", result.getLastName());
        assertEquals("Kardiolog", result.getSpecialization());
        assertNull(result.getInstitutionIds());
    }

    @Test
    void getDoctorByEmail_doctorDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        DoctorNotFoundException resultException = assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctorByEmail(email));
        //then
        assertEquals("Doctor with email: " + email + " do not exists", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void addDoctor_validationPassed_returnDoctorDTO() {
        //given
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("jan")
                .lastName("kowalski")
                .email("test@gmail.com")
                .password("pass")
                .specialization("Kardiolog")
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.save(any())).thenReturn(doctor);
        //when
        DoctorDTO result = doctorService.addDoctor(doctorCommand);
        //then
        assertEquals(1L, result.getId());
        assertEquals("jan", result.getFirstName());
        assertEquals("kowalski", result.getLastName());
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("Kardiolog", result.getSpecialization());
        assertNull(result.getInstitutionIds());
    }

    @Test
    void addDoctor_requestFieldIsNull_exceptionThrown() {
        //given
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("jan")
                .lastName(null)
                .email("test@gmail.com")
                .password("pass")
                .specialization("Kardiolog")
                .build();
        //when_then
        DoctorDataIsNullException resultException = assertThrows(DoctorDataIsNullException.class, () -> doctorService.addDoctor(doctorCommand));
        //then
        assertEquals("Doctor fields cannot be null", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void addDoctor_doctorAlreadyExists_exceptionThrown() {
        //given
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("jan")
                .lastName("kowalski")
                .email("test@gmail.com")
                .password("pass")
                .specialization("Kardiolog")
                .build();
        when(doctorRepository.existsByEmail("test@gmail.com")).thenReturn(true);
        //when_then
        DoctorAlreadyExistException resultException = assertThrows(DoctorAlreadyExistException.class, () -> doctorService.addDoctor(doctorCommand));
        //then
        assertEquals("Doctor with email: test@gmail.com already exists", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void deleteDoctorByEmail_doctorExists_deleteDoctor() {
        //given
        String email = "test@gmail.com";
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when
        doctorService.deleteDoctorByEmail(email);
        //then
        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void deleteDoctorByEmail_doctorDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        DoctorNotFoundException resultException = assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctorByEmail(email));
        //then
        assertEquals("Doctor with email: " + email + " do not exists", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void editDoctorByEmail_doctorExistsValidationPassed_returnDoctorDTO() {
        //given
        String email = "test@gmail.com";
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("krzysztof")
                .lastName("nowak")
                .email("new@gmail.com")
                .password("pass")
                .specialization("Kardiolog")
                .build();
        Doctor currentDoctor = buildDoctor();
        Doctor updatedDoctor = Doctor.builder()
                .id(1L)
                .email("new@gmail.com")
                .password("pass")
                .firstName("krzysztof")
                .lastName("nowak")
                .specialization("Kardiolog")
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(currentDoctor));
        when(doctorRepository.save(any())).thenReturn(updatedDoctor);
        //when
        DoctorDTO resultDoctorDTO = doctorService.editDoctorByEmail(email, doctorCommand);
        //then
        assertEquals(1L, resultDoctorDTO.getId());
        assertEquals("krzysztof", resultDoctorDTO.getFirstName());
        assertEquals("nowak", resultDoctorDTO.getLastName());
        assertEquals("new@gmail.com", resultDoctorDTO.getEmail());
        assertEquals("Kardiolog", resultDoctorDTO.getSpecialization());
        assertNull(resultDoctorDTO.getInstitutionIds());
    }

    @Test
    void editDoctorByEmail_requestFieldIsNull_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("jan")
                .lastName(null)
                .email("test@gmail.com")
                .password("pass")
                .specialization("Kardiolog")
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when_then
        DoctorDataIsNullException resultException = assertThrows(DoctorDataIsNullException.class, () -> doctorService.editDoctorByEmail(email, doctorCommand));
        //then
        assertEquals("Doctor fields cannot be null", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void editDoctorByEmail_newEmailIsReserved_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        DoctorCommand doctorCommand = DoctorCommand.builder()
                .firstName("krzysztof")
                .lastName("nowak")
                .email("new@gmail.com")
                .password("pass")
                .specialization("Kardiolog")
                .build();
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmail(email)).thenReturn(true);
        //when_then
        DoctorAlreadyExistException resultException = assertThrows(DoctorAlreadyExistException.class, () -> doctorService.editDoctorByEmail(email, doctorCommand));
        //them
        assertEquals("Doctor with email: " + doctorCommand.getEmail() + " already exists", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void addInstitution_doctorExistsInstitutionExists_addInstitutionToDoctor() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        Doctor doctor = buildDoctor();
        Institution institution = Institution.builder()
                .id(1L)
                .name("szpital")
                .city("lodz")
                .zipCode("12123")
                .street("narutowicza")
                .buildingNumber("12")
                .build();
        Set<Institution> institutions = new HashSet<>();
        Doctor updatedDoctor = Doctor.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("pass")
                .firstName("jan")
                .lastName("kowalski")
                .specialization("Kardiolog")
                .institutions(institutions)
                .build();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(institutionRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(institution));
        when(doctorRepository.save(any())).thenReturn(updatedDoctor);
        //when
        DoctorDTO resultDoctorDTO = doctorService.addInstitution(email, id);
        //then
        assertEquals(1L, resultDoctorDTO.getInstitutionIds().getFirst());
    }

    @Test
    void addInstitution_doctorDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.empty());
        //when_then
        DoctorNotFoundException resultException = assertThrows(DoctorNotFoundException.class, () -> doctorService.addInstitution(email, id));
        //then
        assertEquals("Doctor with email: " + email + " do not exists", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }

    @Test
    void addInstitution_institutionDoesNotExists_exceptionThrown() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        Doctor doctor = buildDoctor();
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(institutionRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());
        //when_then
        InstitutionNotFoundException resultException = assertThrows(InstitutionNotFoundException.class, () -> doctorService.addInstitution(email, id));
        //then
        assertEquals("Instituion with id: " + id + " do not exists", resultException.getMessage());
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
