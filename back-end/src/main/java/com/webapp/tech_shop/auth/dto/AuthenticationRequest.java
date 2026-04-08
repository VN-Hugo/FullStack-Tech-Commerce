package com.webapp.tech_shop.security.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequest(
    String email,
    String password
) {} 