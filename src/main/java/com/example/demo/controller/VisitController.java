package com.example.demo.controller;

import com.example.demo.model.ErrorMessage;
import com.example.demo.model.command.ReserveVisitCommand;
import com.example.demo.model.command.VisitCommand;
import com.example.demo.model.PageContent;
import com.example.demo.model.command.VisitDayCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Visit operations")
@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Create new visit and add it to existing doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit added to database",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor you wanted to add a new visit to does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))}),
            @ApiResponse(responseCode = "400", description =
                            "Visit already exist, " +
                            "visit fields are null, " +
                            "Date and time visit are from the past, " +
                            "Visit was not scheduled according to the 15-minute breaks, " +
                            "Start date of visit is later than the end date of visit, " +
                            "Visit overlaps with another visit, " +
                            "Visit was not separated by a 15-minute break",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PostMapping("/{doctorEmail}")
    public VisitDTO createVisit(@RequestBody VisitCommand visitCommand, @PathVariable("doctorEmail") String email) {
        return visitService.createVisit(email, visitCommand);
    }

    @Operation(summary = "Get available visits specified by patient, doctor, doctor's specialization or day with visits number, current page and total page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visits list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageContent.class))})
    })
    @GetMapping("/available")
    public PageContent<VisitDTO> getAvailableVisits(
            Pageable pageable,
            @RequestParam(required = false) String patientEmail,
            @RequestParam(required = false) String doctorEmail,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) VisitDayCommand visitDayCommand) {
        return visitService.getAvailableVisitsProcessor(pageable, patientEmail, doctorEmail, specialization, visitDayCommand);
    }

    @Operation(summary = "Reserve visit by given visit's id and patient's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit was reserved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = VisitDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient or visit not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PatchMapping("/reservation")
    public VisitDTO reserveVisit(@RequestBody ReserveVisitCommand reserveVisitCommand) {
        return visitService.reserveVisit(reserveVisitCommand.getPatientEmail(), reserveVisitCommand.getVisitId());
    }

    @Operation(summary = "Get all visits with visits number, current page and total page number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visits list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageContent.class))})
    })
    @GetMapping
    public PageContent<VisitDTO> getVisits(Pageable pageable) {
        return visitService.getVisits(pageable);
    }
}