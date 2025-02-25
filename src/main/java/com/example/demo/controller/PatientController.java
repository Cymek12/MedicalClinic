package com.example.demo.controller;

import com.example.demo.model.NewPassword;
import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable("email") String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
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
    public void editPassword(@PathVariable("email") String email, @RequestBody NewPassword newPassword) {
        patientService.editPassword(email, newPassword);
    }
}
