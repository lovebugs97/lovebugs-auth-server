package com.lovebugs.auth.utils;

import com.lovebugs.auth.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtTokenProvider(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.accessToken.expiration}") long accessTokenExpTime,
            @Value("${jwt.refreshToken.expiration}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public TokenResponse generateToken(Authentication authentication) {
        // 인증 객체로부터 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.info("Authorities: {}", authorities);

        // 토큰 생성
        String accessToken = createToken(authorities, authentication.getName(), accessTokenExpTime);
        String refreshToken = createToken(authorities, authentication.getName(), refreshTokenExpTime);

        log.info("AccessToken: {}", accessToken);
        log.info("RefreshToken: {}", refreshToken);

        return new TokenResponse("Bearer", accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String createToken(String authorities, String email, long tokenExpTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("authorities", authorities);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
