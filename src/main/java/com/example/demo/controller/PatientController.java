package com.example.demo.controller;

import com.example.demo.model.PasswordRequest;
import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.PageContent;
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
    public PageContent<PatientDTO> getPatients(Pageable pageable) {
        return patientService.getPatients(pageable);
    }

    @GetMapping("/{email}")
    public PatientDTO getPatientByEmail(@PathVariable("email") String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPatient(@RequestBody PatientCommand patientCommand) {
        patientService.addPatient(patientCommand);
    }

    @DeleteMapping("/{email}")
    public void deletePatientByEmail(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public PatientDTO editPatient(@PathVariable("email") String email, @RequestBody PatientCommand patientCommand) {
        return patientService.editPatient(email, patientCommand);
    }

    @PatchMapping("/{email}")
    public void editPassword(@PathVariable("email") String email, @RequestBody PasswordRequest newPassword) {
        patientService.editPassword(email, newPassword);
    }
}
