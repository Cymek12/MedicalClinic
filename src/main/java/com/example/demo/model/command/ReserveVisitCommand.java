package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReserveVisitCommand {
    private String patientEmail;
    private String visitId;
}
