package com.example.demo.controller;

import com.example.demo.model.ErrorMessage;
import com.example.demo.model.command.InstitutionCommand;
import com.example.demo.model.dto.FullInstitutionDTO;
import com.example.demo.model.PageContent;
import com.example.demo.model.dto.InstitutionDTO;
import com.example.demo.service.InstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Institution operations")
@RestController
@RequestMapping("/institutions")
@RequiredArgsConstructor
public class InstitutionController {
    private final InstitutionService institutionService;

    @Operation(summary = "Add new institution to database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution added to database",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Institution already exist or institution fields are null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @PostMapping
    public InstitutionDTO addInstitution(@RequestBody InstitutionCommand institutionCommand) {
        return institutionService.addInstitution(institutionCommand);
    }

    @Operation(summary = "Get all institutions with institutions number, current page and total page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institutions list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageContent.class))}),
    })
    @GetMapping
    public PageContent<InstitutionDTO> getInstitutions(Pageable pageable) {
        return institutionService.getInstitutions(pageable);
    }

    @Operation(summary = "Get institution by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution's details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Institution not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/{name}")
    public InstitutionDTO getInstitutionByName(@PathVariable("name") String name) {
        return institutionService.getInstitutionByName(name);
    }

    @Operation(summary = "Edit the details of the institution found by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution with changed data",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Institution not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Institution fields are null or new institution's name is reserved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PutMapping("/{name}")
    public InstitutionDTO editInstitutionByName(@PathVariable("name") String name, @RequestBody InstitutionCommand institutionCommand) {
        return institutionService.editInstitutionByName(name, institutionCommand);
    }

    @Operation(summary = "Delete institution from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution was deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Institution not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @DeleteMapping("/{name}")
    public void deleteInstitutionByName(@PathVariable("name") String name) {
        institutionService.deleteInstitutionByName(name);
    }

    @Operation(summary = "Add institutions with doctors to database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institutions with doctors was added successfully")
    })
    @PostMapping("/bulk")
    public void addInstitutionsWithDoctors(@RequestBody List<FullInstitutionDTO> fullInstitutionDTOs) {
        institutionService.addInstitutionsWithDoctors(fullInstitutionDTOs);
    }
}
