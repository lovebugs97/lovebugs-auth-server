package com.lovebugs.auth.utils;

import com.lovebugs.auth.config.JwtProperties;
import com.lovebugs.auth.domain.enums.RoleType;
import com.lovebugs.auth.dto.auth.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private final Key key;
    private final JwtParser jwtParser;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
    private final String prefix;

    public JwtUtils(JwtProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
        this.prefix = jwtProperties.getPrefix();
    }

    public TokenDto generateToken(RoleType role, String email) {
        String accessToken = createToken(String.valueOf(role), email, accessTokenExpiration);
        String refreshToken = createToken(String.valueOf(role), email, refreshTokenExpiration);

        return new TokenDto(prefix, accessToken, refreshToken);
    }

    public Date extractExpiration(String token) {
        return extractField(token, "exp", Date.class);
    }

    public String extractEmail(String token) {
        return extractField(token, "email", String.class);
    }

    public String extractRole(String token) {
        return extractField(token, "role", String.class);
    }

    private <T> T extractField(String token, String field, Class<T> tClass) {
        try {
            if (validateToken(token)) {
                Jws<Claims> claims = jwtParser.parseClaimsJws(token);
                return claims.getBody().get(field, tClass);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = jwtParser.parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token Expired: {}", e.getMessage());
            return false;
        }
    }

    private String createToken(String role, String email, long tokenExpTime) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("role", role);

        Date local = new Date();
        Date expirationDate = new Date(local.getTime() + (tokenExpTime * 1000));
        log.info("local: {}, expirationDate: {}", local, expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(local)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
