package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class VisitDTO {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private DoctorDTO doctorDTO;
    private PatientDTO patientDTO;
}
