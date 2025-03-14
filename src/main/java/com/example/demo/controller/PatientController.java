package com.example.demo.controller;

import com.example.demo.model.PasswordRequest;
import com.example.demo.model.dto.PageContentDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public PageContentDTO<PatientDTO> getPatients(Pageable pageable) {
        return patientService.getPatients(pageable);
    }

    @GetMapping("/{email}")
    public PatientDTO getPatientByEmail(@PathVariable("email") String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatientByEmail(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public void editPatient(@PathVariable("email") String email, @RequestBody Patient newPatientData) {
        patientService.editPatient(email, newPatientData);
    }

    @PatchMapping("/{email}")
    public void editPassword(@PathVariable("email") String email, @RequestBody PasswordRequest newPassword) {
        patientService.editPassword(email, newPassword);
    }
}
