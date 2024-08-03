package com.lovebugs.auth.utils;

import com.lovebugs.auth.config.JwtProperties;
import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.dto.auth.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtProperties = jwtProperties;

        log.info("SecretKey: {}", jwtProperties.getSecretKey());
    }

    public TokenDto generateToken(Member member) {
        String authorities = String.join(",", member.getRoles());
        String accessToken = createToken(authorities, member.getEmail(), jwtProperties.getAccessTokenExpiration());
        String refreshToken = createToken(authorities, member.getEmail(), jwtProperties.getRefreshTokenExpiration());

        return new TokenDto(jwtProperties.getPrefix(), accessToken, refreshToken);
    }

    public Date extractExpiration(String token) {
        if (validateToken(token)) {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().getExpiration();
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token Expired: {}", e.getMessage());
            return false;
        }
    }

    private String createToken(String authorities, String email, long tokenExpTime) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("authorities", authorities);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (tokenExpTime * 1000));
        log.info("now: {}, expirationDate: {}", now, expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
