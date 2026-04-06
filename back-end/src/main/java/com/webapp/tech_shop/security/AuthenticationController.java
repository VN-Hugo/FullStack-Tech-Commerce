package com.webapp.tech_shop.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.tech_shop.security.dto.AuthenticationRequest;
import com.webapp.tech_shop.security.dto.AuthenticationResponse;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    
}
