package com.webapp.tech_shop.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


import com.webapp.tech_shop.auth.dto.AuthenticationRequest;
import com.webapp.tech_shop.auth.dto.AuthenticationResponse;
import com.webapp.tech_shop.auth.dto.RefreshTokenRequest;
import com.webapp.tech_shop.security.TokenService;
import com.webapp.tech_shop.security.jwt.JwtService;


import com.webapp.tech_shop.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    //login
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userService.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.saveTokens(user, jwtToken, refreshToken);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    //refresh token
    public AuthenticationResponse refresh(RefreshTokenRequest request) {
        final String refreshToken = request.refreshToken();
        final String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null)
        {
          var user = userService.findByEmail(userEmail)
          .orElseThrow(() -> new RuntimeException("User not found"));
          if(jwtService.isRefreshTokenValid(refreshToken, user))
          {
            var jwtToken = jwtService.generateAccessToken(user);
            var newRefreshToken = jwtService.generateRefreshToken(user);
            tokenService.saveTokens(user, jwtToken, newRefreshToken);
            return AuthenticationResponse
                    .builder()
                    .accessToken(jwtToken)
                    .refreshToken(newRefreshToken)
                    .build();
          }
        }
        throw new RuntimeException("Refresh token không hợp lệ!");
    }
    //logout
}
