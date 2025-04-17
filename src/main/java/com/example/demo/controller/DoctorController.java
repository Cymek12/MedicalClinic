package com.example.demo.controller;

import com.example.demo.model.ErrorMessage;
import com.example.demo.model.command.AddInstitutionCommand;
import com.example.demo.model.command.DoctorCommand;
import com.example.demo.model.PageContent;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Doctor operations")
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Add new doctor to database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor added to database",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor already exist or doctor fields are null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @PostMapping
    public DoctorDTO addDoctor(@RequestBody DoctorCommand doctorCommand) {
        return doctorService.addDoctor(doctorCommand);
    }

    @Operation(summary = "Get all doctors with doctors number, current page and total page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageContent.class))}),
    })
    @GetMapping
    public PageContent<DoctorDTO> getDoctors(Pageable pageable) {
        return doctorService.getDoctors(pageable);
    }

    @Operation(summary = "Get doctor by his email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor's details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/{email}")
    public DoctorDTO getDoctorByEmail(@PathVariable("email") String email) {
        return doctorService.getDoctorByEmail(email);
    }

    @Operation(summary = "Edit the details of the doctor found by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor with changed data",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor fields are null or new doctor's email is reserved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PutMapping("/{email}")
    public DoctorDTO editDoctorByEmail(@PathVariable("email") String email, @RequestBody DoctorCommand doctorCommand) {
        return doctorService.editDoctorByEmail(email, doctorCommand);
    }

    @Operation(summary = "Delete doctor from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor was deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @DeleteMapping("/{email}")
    public void deleteDoctorByEmail(@PathVariable("email") String email) {
        doctorService.deleteDoctorByEmail(email);
    }

    @Operation(summary = "Add institution to doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution was added to doctor successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor or institution not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
    })
    @PatchMapping("/institution")
    public DoctorDTO addInstitution(@RequestBody AddInstitutionCommand addInstitutionCommand) {
        return doctorService.addInstitution(addInstitutionCommand.getDoctorEmail(), addInstitutionCommand.getInstitutionId());
    }
}