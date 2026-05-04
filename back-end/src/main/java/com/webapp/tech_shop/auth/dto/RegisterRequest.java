package com.webapp.tech_shop.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank(message = "Name must not be empty")
        String name,
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password must not be empty")
        String password
) {}
