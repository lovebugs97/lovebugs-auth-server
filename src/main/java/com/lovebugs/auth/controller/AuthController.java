package com.lovebugs.auth.controller;

import com.lovebugs.auth.dto.LoginRequest;
import com.lovebugs.auth.dto.SignupRequest;
import com.lovebugs.auth.dto.TokenResponse;
import com.lovebugs.auth.service.AuthService;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupRequest> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);

        return ResponseEntity.ok()
                .body(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login Request: {}", loginRequest.toString());
        TokenResponse tokenResponse = authService.login(loginRequest);

        ResponseCookie accessTokenCookie = ResponseCookie.from(ACCESS_TOKEN, tokenResponse.accessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN, tokenResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, "")
                .maxAge(1)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(ACCESS_TOKEN) String accessToken,
            @CookieValue(REFRESH_TOKEN) String refreshToken
    ) {
        System.out.println(accessToken);
        System.out.println(refreshToken);
        return ResponseEntity.ok(null);
    }
}
