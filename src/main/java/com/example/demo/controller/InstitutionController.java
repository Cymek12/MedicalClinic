package com.example.demo.controller;

import com.example.demo.model.command.InstitutionCommand;
import com.example.demo.model.dto.FullInstitutionDTO;
import com.example.demo.model.PageContent;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institutions")
@RequiredArgsConstructor
public class InstitutionController {
    private final InstitutionService institutionService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addInstitution(@RequestBody InstitutionCommand institutionCommand) {
        institutionService.addInstitution(institutionCommand);
    }

    @GetMapping
    public PageContent<InstitutionDTO> getInstitutions(Pageable pageable) {
        return institutionService.getInstitutions(pageable);
    }

    @GetMapping("/{name}")
    public InstitutionDTO getInstitutionByName(@PathVariable("name") String name) {
        return institutionService.getInstitutionByName(name);
    }

    @PutMapping("/{name}")
    public void editInstitutionByName(@PathVariable("name") String name, @RequestBody InstitutionCommand institutionCommand) {
        institutionService.editInstitutionByName(name, institutionCommand);
    }

    @DeleteMapping("/{name}")
    public void deleteInstitutionByName(@PathVariable("name") String name) {
        institutionService.deleteInstitutionByName(name);
    }

    @PostMapping("/bulk")
    public void addInstitutionsWithDoctors(@RequestBody List<FullInstitutionDTO> fullInstitutionDTOs) {
        institutionService.addInstitutionsWithDoctors(fullInstitutionDTOs);
    }
}
