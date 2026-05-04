package com.webapp.tech_shop.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(
    @JsonProperty("refresh_token")
    @NotBlank(message = "Refresh token must not be empty")
    String refreshToken
) 
{}
