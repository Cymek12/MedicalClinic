package com.example.demo.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class VisitCommand {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
