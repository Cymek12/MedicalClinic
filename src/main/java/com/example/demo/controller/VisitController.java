package com.example.demo.controller;

import com.example.demo.model.command.ReserveVisitCommand;
import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.VisitDayCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @PostMapping("/{doctorEmail}")
    public VisitDTO createVisit(@RequestBody VisitCommand visitCommand, @PathVariable("doctorEmail") String email) {
        return visitService.createVisit(email, visitCommand);
    }

    @GetMapping("/available")
    public PageContent<VisitDTO> getAvailableVisits(Pageable pageable) {
        return visitService.getAvailableVisits(pageable);
    }

    @PatchMapping("/reservation")
    public VisitDTO reserveVisit(@RequestBody ReserveVisitCommand reserveVisitCommand) {
        return visitService.reserveVisit(reserveVisitCommand.getPatientEmail(), reserveVisitCommand.getVisitId());
    }

    @GetMapping
    public PageContent<VisitDTO> getVisits(Pageable pageable) {
        return visitService.getVisits(pageable);
    }

    @GetMapping("/byPatient")
    public PageContent<VisitDTO> getVisitsByPatient(@RequestParam(required = false) String patientEmail, Pageable pageable) {
        return visitService.getVisitsByPatient(patientEmail, pageable);
    }

    @GetMapping("/available/{doctorEmail}")
    public PageContent<VisitDTO> getAvailableVisitsByDoctor(@PathVariable("doctorEmail") String doctorEmail, Pageable pageable) {
        return visitService.getAvailableVisitsByDoctor(doctorEmail, pageable);
    }

    @GetMapping("/available/specialization")
    PageContent<VisitDTO> getAvailableVisitsByDayAndSpecialization(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) VisitDayCommand visitDayCommand,
            Pageable pageable
    ){
        return visitService.getAvailableVisitsByDayAndSpecialization(visitDayCommand, specialization, pageable);
    }
}
