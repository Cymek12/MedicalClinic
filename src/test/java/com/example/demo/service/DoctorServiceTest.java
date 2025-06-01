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
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.*;

import static com.example.demo.TestDataBuilder.*;
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
        List<Doctor> doctors = List.of(
                new Doctor(1L, "jan", "kowalski", "test@gmail.com", "pass", "Kardiolog", new HashSet<>(), new ArrayList<>()),
                new Doctor(2L, "krzysztof", "nowak", "test2@gmail.com", "pass2", "Okulista", new HashSet<>(), new ArrayList<>())
        );
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);
        //when
        PageContent<DoctorDTO> result = doctorService.getDoctors(pageable);
        //then
        assertEquals(2L, result.getTotalElements());
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getTotalPageNumber());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("jan", result.getContent().get(0).getFirstName());
        assertEquals("kowalski", result.getContent().get(0).getLastName());
        assertEquals("test@gmail.com", result.getContent().get(0).getEmail());
        assertEquals("Kardiolog", result.getContent().get(0).getSpecialization());
        assertNull(result.getContent().get(0).getInstitutionIds());
        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals("krzysztof", result.getContent().get(1).getFirstName());
        assertEquals("nowak", result.getContent().get(1).getLastName());
        assertEquals("test2@gmail.com", result.getContent().get(1).getEmail());
        assertEquals("Okulista", result.getContent().get(1).getSpecialization());
        assertNull(result.getContent().get(1).getInstitutionIds());
    }

    @Test
    void getDoctorByEmail_doctorExists_returnDoctorDTO() {
        //given
        String email = "test@gmail.com";
        Doctor foundDoctor = buildDoctor("test@gmail.com");
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
    void getDoctorByEmail_doctorDoesNotExists_DoctorNotFoundExceptionThrown() {
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
        Doctor doctor = buildDoctor("test@gmail.com");
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
    void addDoctor_requestFieldIsNull_DoctorDataIsNullExceptionThrown() {
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
    void addDoctor_doctorAlreadyExists_DoctorAlreadyExistExceptionThrown() {
        //given
        DoctorCommand doctorCommand = buildDoctorCommand("test@gmail.com");
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
        Doctor doctor = buildDoctor("test@gmail.com");
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when
        doctorService.deleteDoctorByEmail(email);
        //then
        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void deleteDoctorByEmail_doctorDoesNotExists_DoctorNotFoundExceptionThrown() {
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
        DoctorCommand doctorCommand = buildDoctorCommand("new@gmail.com");
        Doctor currentDoctor = buildDoctor("test@gmail.com");
        Doctor updatedDoctor = buildDoctor("new@gmail.com");
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(currentDoctor));
        when(doctorRepository.save(any())).thenReturn(updatedDoctor);
        //when
        DoctorDTO resultDoctorDTO = doctorService.editDoctorByEmail(email, doctorCommand);
        //then
        assertEquals(1L, resultDoctorDTO.getId());
        assertEquals("jan", resultDoctorDTO.getFirstName());
        assertEquals("kowalski", resultDoctorDTO.getLastName());
        assertEquals("new@gmail.com", resultDoctorDTO.getEmail());
        assertEquals("Kardiolog", resultDoctorDTO.getSpecialization());
        assertNull(resultDoctorDTO.getInstitutionIds());
    }

    @Test
    void editDoctorByEmail_requestFieldIsNull_DoctorDataIsNullExceptionThrown() {
        //given
        String email = "test@gmail.com";
        DoctorCommand doctorCommand = buildDoctorCommand(null);
        Doctor doctor = buildDoctor("test@gmail.com");
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        //when_then
        DoctorDataIsNullException resultException = assertThrows(DoctorDataIsNullException.class, () -> doctorService.editDoctorByEmail(email, doctorCommand));
        //then
        assertEquals("Doctor fields cannot be null", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void editDoctorByEmail_newEmailIsReserved_DoctorAlreadyExistExceptionThrown() {
        //given
        String email = "test@gmail.com";
        DoctorCommand doctorCommand = buildDoctorCommand("new@gmail.com");
        Doctor doctor = buildDoctor("test@gmail.com");
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmail(email)).thenReturn(true);
        //when_then
        DoctorAlreadyExistException resultException = assertThrows(DoctorAlreadyExistException.class, () -> doctorService.editDoctorByEmail(email, doctorCommand));
        //them
        assertEquals("Doctor with email: " + doctorCommand.getEmail() + " already exists", resultException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, resultException.getHttpStatus());
    }

    @Test
    void addInstitution_DataCorrect_InstitutionAssigned() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        Doctor doctor = buildDoctor("test@gmail.com");
        Institution institution = buildInstitution("szpital");
        Set<Institution> institutions = Collections.singleton(institution);
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(institutionRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(institution));
        //when
        doctorService.addInstitution(email, id);
        //then
        ArgumentCaptor<Doctor> doctorCaptor = ArgumentCaptor.forClass(Doctor.class);
        verify(doctorRepository).save(doctorCaptor.capture());

        assertEquals(institutions, doctorCaptor.getValue().getInstitutions());
    }

    @Test
    void addInstitution_doctorDoesNotExists_DoctorNotFoundExceptionThrown() {
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
    void addInstitution_institutionDoesNotExists_InstitutionNotFoundExceptionThrown() {
        //given
        String email = "test@gmail.com";
        String id = "1";
        Doctor doctor = buildDoctor("test@gmail.com");
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(doctor));
        when(institutionRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());
        //when_then
        InstitutionNotFoundException resultException = assertThrows(InstitutionNotFoundException.class, () -> doctorService.addInstitution(email, id));
        //then
        assertEquals("Institution with id: " + id + " do not exists", resultException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, resultException.getHttpStatus());
    }
}