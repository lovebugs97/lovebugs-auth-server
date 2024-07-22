package com.lovebugs.auth.utils;

import com.lovebugs.auth.config.JwtProperties;
import com.lovebugs.auth.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {
    private final Key key;
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtProperties = jwtProperties;
    }

    public TokenDto generateToken(Authentication authentication) {
        // 인증 객체로부터 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 생성
        String accessToken = createToken(authorities, authentication.getName(), jwtProperties.getAccessTokenExpiration());
        String refreshToken = createToken(authorities, authentication.getName(), jwtProperties.getRefreshTokenExpiration());

        return new TokenDto(jwtProperties.getPrefix(), accessToken, refreshToken);
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
        Claims claims = Jwts.claims();
        claims.put("email", email);
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
