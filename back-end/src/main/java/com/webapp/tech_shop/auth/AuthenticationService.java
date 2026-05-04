package com.webapp.tech_shop.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.tech_shop.auth.dto.AuthenticationRequest;
import com.webapp.tech_shop.auth.dto.AuthenticationResponse;
import com.webapp.tech_shop.auth.dto.RefreshTokenRequest;
import com.webapp.tech_shop.auth.dto.RegisterRequest;
import com.webapp.tech_shop.exception.BaseException;
import com.webapp.tech_shop.exception.ErrorCode;
import com.webapp.tech_shop.security.TokenService;
import com.webapp.tech_shop.security.jwt.JwtService;
import com.webapp.tech_shop.user.Role;
import com.webapp.tech_shop.user.User;
import com.webapp.tech_shop.user.UserService;

import io.jsonwebtoken.JwtException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        userService.findByEmail(request.email()).ifPresent(user -> {
            throw new BaseException(ErrorCode.USER_ALREADY_EXISTS);
        });

        var savedUser = userService.save(User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build());

        return buildAuthenticationResponse(savedUser);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new BaseException(ErrorCode.INVALID_CREDENTIALS);
        }

        var user = userService.findByEmail(request.email())
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        return buildAuthenticationResponse(user);
    }

    public AuthenticationResponse refresh(RefreshTokenRequest request) {
        try {
            final String refreshToken = request.refreshToken();
            final String userEmail = jwtService.extractUsername(refreshToken);

            if (userEmail == null) {
                throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            var user = userService.findByEmail(userEmail)
                    .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

            if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
                throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            return buildAuthenticationResponse(user);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public void logout(RefreshTokenRequest request) {
        try {
            final String refreshToken = request.refreshToken();
            final String userEmail = jwtService.extractUsername(refreshToken);

            if (userEmail == null) {
                throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            userService.findByEmail(userEmail)
                    .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

            tokenService.revokeAllUserTokensByEmail(userEmail);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {
        var jwtToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.saveTokens(user, jwtToken, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}

