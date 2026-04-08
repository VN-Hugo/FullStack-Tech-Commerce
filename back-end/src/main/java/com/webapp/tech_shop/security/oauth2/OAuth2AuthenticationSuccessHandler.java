package com.webapp.tech_shop.security.oauth2;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;


import com.webapp.tech_shop.security.TokenService;
import com.webapp.tech_shop.user.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.webapp.tech_shop.security.jwt.JwtService;
import com.webapp.tech_shop.user.Role;
import com.webapp.tech_shop.user.User;
import java.util.UUID;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${application.security.oauth2.redirect-uri:http://localhost:3000/oauth2/redirect}")
    private String redirectUri;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
        Authentication authentication) throws IOException,ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        if (email == null || email.isEmpty()) {
        // Trả về lỗi rõ ràng thay vì để hệ thống crash ngầm
        throw new RuntimeException("Email not found from Google.");
        }

        User user = userRepository.findByEmail(email)
        .orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .role(Role.CUSTOMER)
                    .password(UUID.randomUUID().toString())
                    .build();
            return userRepository.save(newUser);
        });
        
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveTokens(user, accessToken, refreshToken);

        String redirectUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
