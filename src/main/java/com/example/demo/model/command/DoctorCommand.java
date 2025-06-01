package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DoctorCommand {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String specialization;
}
