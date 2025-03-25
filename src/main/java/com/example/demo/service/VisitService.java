package com.example.demo.service;

import com.example.demo.exceptions.doctor.DoctorNotFoundException;
import com.example.demo.exceptions.patient.PatientNotFoundException;
import com.example.demo.exceptions.visit.*;
import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.PageContent;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.VisitMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final VisitMapper visitMapper;
    private final PatientRepository patientRepository;

    public VisitDTO createVisit(String email, VisitCommand visitCommand) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with email: " + email + " do not exists"));
        Visit visit = visitMapper.toEntity(visitCommand);
        validateVisit(visit, doctor);
        visit.setDoctor(doctor);
        return visitMapper.toDTO(visitRepository.save(visit));
    }

    public PageContent<VisitDTO> getVisits(Pageable pageable) {
        Page<Visit> visitPage = visitRepository.findAll(pageable);
        List<VisitDTO> visitDTOs = visitMapper.toDTO(visitPage.getContent());
        return new PageContent<>(
                visitPage.getTotalElements(),
                visitPage.getNumber(),
                visitPage.getTotalPages(),
                visitDTOs
        );
    }

    public PageContent<VisitDTO> getAvailableVisits(Pageable pageable) {
        Page<Visit> visitPage = visitRepository.findAll(pageable);
        List<VisitDTO> visitDTOs = visitMapper.toDTO(visitPage.getContent().stream()
                .filter(visit -> Objects.isNull(visit.getPatient()))
                .toList());
        return new PageContent<>(
                visitPage.getTotalElements(),
                visitPage.getNumber(),
                visitPage.getTotalPages(),
                visitDTOs
        );
    }

    private void validateVisit(Visit visit, Doctor doctor) {
        if (visit.isVisitDataNull()) {
            throw new VisitDataIsNullException("Visit data cannot be null");
        }
        if (visit.isDateTimeBeforeNow()) {
            throw new VisitDateTimeFromPastException("Date and time visit cannot be from the past");
        }
        if (!visit.isTimeFormatCorrect()) {
            throw new WrongVisitTimeFormatException("Visit can only be scheduled at 15-minute intervals");
        }
        if (!visit.isEndTimeAfterStartTime()) {
            throw new WrongVisitTimeFormatException("Visit end date time should be after start date time");
        }

        List<Visit> overlappingVisits = visitRepository.findOverlappingVisits(doctor, visit.getStartDateTime(), visit.getEndDateTime());
        if(!overlappingVisits.isEmpty()) {
            throw new VisitOverlapsException("Visit cannot overlaps with another visit");
        }

        List<Visit> violatingBreakVisits = visitRepository.findVisitsViolatingBreak(doctor, visit.getStartDateTime(), visit.getEndDateTime());
        if(!violatingBreakVisits.isEmpty()) {
            throw new VisitViolatesBreakException("Visit should be separated by 15-minute break");
        }
    }

    public void reserveVisit(String email, String id) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with email: " + email + " does not exist"));
        Visit visit = visitRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new VisitNotFoundException("Visit with id: " + id + " does not exist"));
        visit.setPatient(patient);
        visitRepository.save(visit);
    }
}
