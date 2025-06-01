package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AddInstitutionCommand {
    private String doctorEmail;
    private String institutionId;
}
