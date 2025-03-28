package com.example.demo.controller;

import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.PageContent;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public DoctorDTO addDoctor(@RequestBody DoctorCommand doctorCommand) {
        return doctorService.addDoctor(doctorCommand);
    }

    @GetMapping
    public PageContent<DoctorDTO> getDoctors(Pageable pageable) {
        return doctorService.getDoctors(pageable);
    }

    @GetMapping("/{email}")
    public DoctorDTO getDoctorByEmail(@PathVariable("email") String email) {
        return doctorService.getDoctorByEmail(email);
    }

    @PutMapping("/{email}")
    public DoctorDTO editDoctorByEmail(@PathVariable("email") String email, @RequestBody DoctorCommand doctorCommand) {
        return doctorService.editDoctorByEmail(email, doctorCommand);
    }

    @DeleteMapping("/{email}")
    public void deleteDoctorByEmail(@PathVariable("email") String email) {
        doctorService.deleteDoctorByEmail(email);
    }

    @PatchMapping("/institution/{doctorEmail}/{institutionId}")
    public DoctorDTO addInstitution(@PathVariable("doctorEmail") String email, @PathVariable("institutionId") String id) {
        return doctorService.addInstitution(email, id);
    }
}
