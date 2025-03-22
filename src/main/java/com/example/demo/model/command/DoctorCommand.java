package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorCommand {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String specialization;
}
