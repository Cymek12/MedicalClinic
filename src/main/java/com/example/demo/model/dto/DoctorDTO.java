package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private List<Long> institutionIds;
}
