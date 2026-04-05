package com.webapp.tech_shop.security;

import org.springframework.stereotype.Service;
import com.webapp.tech_shop.security.model.Token;
import com.webapp.tech_shop.security.model.TokenType;
import com.webapp.tech_shop.user.User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void revokeAllUserTokensByEmail(String email) {
        var validUserTokens = tokenRepository.findAllTokensByUserEmail(email);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    public void saveTokens(User user, String jwtToken, String refreshToken){
        saveAccessToken(user,jwtToken);
        saveRefreshToken(user,refreshToken);
    }


    private void saveRefreshToken(User user, String jwtToken) {
        saveToken(user, jwtToken, TokenType.REFRESH_TOKEN);
    }

    private void saveAccessToken(User user, String jwtToken) {
        saveToken(user, jwtToken, TokenType.ACCESS_TOKEN);
    }
    
    private void saveToken(User user, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .revoked(false)
                .expired(false)
                .build(); 
        tokenRepository.save(token); 
    }
}
