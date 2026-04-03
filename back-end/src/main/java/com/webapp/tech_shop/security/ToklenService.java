package com.webapp.tech_shop.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToklenService {
    private final TokenRepository tokenRepository;

}
