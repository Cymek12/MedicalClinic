package com.example.demo.controller;

import com.example.demo.model.Doctor;
import com.example.demo.model.DoctorDTO;
import com.example.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDoctor(@RequestBody Doctor doctor) {
        doctorService.addDoctor(doctor);
    }

    @GetMapping
    public List<DoctorDTO> getDoctors() {
        return doctorService.getDoctors();
    }

    @GetMapping("/{email}")
    public DoctorDTO getDoctorByEmail(@PathVariable("email") String email) {
        return doctorService.getDoctorByEmail(email);
    }

    @PutMapping("/{email}")
    public void editDoctorByEmail(@PathVariable("email") String email, @RequestBody Doctor doctor) {
        doctorService.editDoctorByEmail(email, doctor);
    }

    @DeleteMapping("/{email}")
    public void deleteDoctorByEmail(@PathVariable("email") String email) {
        doctorService.deleteDoctorByEmail(email);
    }

    @PatchMapping("/addInstitution/{email}/{id}")
    public void addInstitution(@PathVariable("email") String email, @PathVariable("id") String id){
        doctorService.addInstitution(email, id);
    }
}
