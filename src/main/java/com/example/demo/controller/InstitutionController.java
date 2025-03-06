package com.example.demo.controller;

import com.example.demo.model.Institution;
import com.example.demo.model.InstitutionDTO;
import com.example.demo.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {
    private final InstitutionService institutionService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addInstitution(@RequestBody Institution institution) {
        institutionService.addInstitution(institution);
    }

    @GetMapping
    public List<InstitutionDTO> getInstitutions() {
        return institutionService.getInstitutions();
    }

    @GetMapping("/{name}")
    public InstitutionDTO getInstitutionByName(@PathVariable("name") String name) {
        return institutionService.getInstitutionByName(name);
    }

    @PutMapping("/{name}")
    public void editInstitutionByName(@PathVariable("name") String name, @RequestBody Institution institution) {
        institutionService.editInstitutionByName(name, institution);
    }

    @DeleteMapping("/{name}")
    public void deleteInstitutionByName(@PathVariable("name") String name) {
        institutionService.deleteInstitutionByName(name);
    }
}
