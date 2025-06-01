package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class VisitDayCommand {
    private LocalDate localDate;
}
