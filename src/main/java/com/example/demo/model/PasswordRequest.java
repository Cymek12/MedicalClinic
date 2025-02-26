package com.example.demo.model;

import lombok.Builder;

@Builder
public record PasswordRequest(String newPassword) {
}
