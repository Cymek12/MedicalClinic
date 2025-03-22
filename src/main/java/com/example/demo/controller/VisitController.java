package com.example.demo.controller;

import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.PageContent;
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
    public void createVisit(@RequestBody VisitCommand visitCommand, @PathVariable("doctorEmail") String email) {
        visitService.createVisit(email, visitCommand);
    }

    @GetMapping("/available")
    public PageContent<VisitDTO> getAvailableVisits(Pageable pageable) {
        return visitService.getAvailableVisits(pageable);
    }

    @PatchMapping("/{patientEmail}/{visitId}")
    public void reserveVisit(@PathVariable("patientEmail") String email, @PathVariable("visitId") String id) {
        visitService.reserveVisit(email, id);
    }

    @GetMapping
    public PageContent<VisitDTO> getVisits(Pageable pageable) {
        return visitService.getVisits(pageable);
    }
}
