package com.webapp.tech_shop.security.jwt;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.webapp.tech_shop.security.TokenRepository;
import com.webapp.tech_shop.security.model.TokenType;

import lombok.RequiredArgsConstructor;
import java.util.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenRepository tokenRepository;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

       public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtRefreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration)
    {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    } 
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); //&& !isTokenRevoked(token);
    }

    // private boolean isTokenRevoked(String token) {
    //     return tokenRepository.findByToken(token)
    //             .map(t -> t.isRevoked() || t.isExpired())
    //             .orElse(true);
    // }
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return isTokenvalid(token, userDetails, TokenType.ACCESS_TOKEN);
    }

    private boolean isTokenvalid(String token, UserDetails userDetails, TokenType tokenType) {
        if (token == null) {
            return false;
    }
        var tokenDB = tokenRepository.findByToken(token).orElse(null);
        if (tokenDB == null || 
            tokenDB.isExpired() || 
            tokenDB.isRevoked() || 
            tokenDB.getTokenType() != tokenType) { 
            return false;
        }
        // check user, secretKey, time expired
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
