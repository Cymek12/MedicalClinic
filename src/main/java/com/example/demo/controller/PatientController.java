package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getPatients(){
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable("email") String email){
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    public void addNewPatient(@RequestBody Patient patient){
        patientService.addNewPatient(patient);
    }

    @DeleteMapping("/{email}")
    public void deletePatientByEmail(@PathVariable("email") String email){
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public boolean editPatient(@PathVariable("email") String email, @RequestBody Patient newPatient) {
        return patientService.editPatient(email, newPatient);
    }
}
