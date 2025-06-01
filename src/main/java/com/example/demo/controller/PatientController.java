package com.example.demo.controller;

import com.example.demo.model.ErrorMessage;
import com.example.demo.model.PasswordRequest;
import com.example.demo.model.command.PatientCommand;
import com.example.demo.model.PageContent;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Patient operations")
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Get all patients with patients number, current page and total page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageContent.class))}),
    })
    @GetMapping
    public PageContent<PatientDTO> getPatients(Pageable pageable) {
        return patientService.getPatients(pageable);
    }

    @Operation(summary = "Get patient by his email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient's details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/{email}")
    public PatientDTO getPatientByEmail(@PathVariable("email") String email) {
        return patientService.getPatientByEmail(email);
    }

    @Operation(summary = "Add new patient to database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient added to database",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient already exist or patient fields are null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @PostMapping
    public PatientDTO addPatient(@RequestBody PatientCommand patientCommand) {
        return patientService.addPatient(patientCommand);
    }

    @Operation(summary = "Delete patient from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient was deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @DeleteMapping("/{email}")
    public void deletePatientByEmail(@PathVariable("email") String email) {
        patientService.deletePatientByEmail(email);
    }

    @Operation(summary = "Edit the details of the patient found by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient with changed data",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Patient fields are null, new patient's email is reserved or trying to change idCardNo",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PutMapping("/{email}")
    public PatientDTO editPatient(@PathVariable("email") String email, @RequestBody PatientCommand patientCommand) {
        return patientService.editPatient(email, patientCommand);
    }

    @Operation(summary = "Edit patient's password found by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient with changed password",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PatchMapping("/{email}")
    public PatientDTO editPassword(@PathVariable("email") String email, @RequestBody PasswordRequest newPassword) {
        return patientService.editPassword(email, newPassword);
    }
}
