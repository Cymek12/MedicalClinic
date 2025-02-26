package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Patient {
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private final String idCardNo;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String phoneNumber;
    @NonNull
    private LocalDate birthday;

}
