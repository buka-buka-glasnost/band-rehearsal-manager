package com.bandrehearsal.bandrehearsalmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters long")
        @Pattern(regexp = "[A-Za-z0-9_.-]+", message = "Username may contain letters, digits, '.', '_' and '-'")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be 6-100 characters long")
        String password) {
}
