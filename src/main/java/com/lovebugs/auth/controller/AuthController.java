package com.lovebugs.auth.controller;

import com.lovebugs.auth.config.JwtProperties;
import com.lovebugs.auth.dto.LoginRequest;
import com.lovebugs.auth.dto.LoginResponse;
import com.lovebugs.auth.dto.SignupRequest;
import com.lovebugs.auth.service.AuthService;
import com.lovebugs.auth.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
@Slf4j
public class AuthController {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private final AuthService authService;
    private final CookieUtils cookieUtils;
    private final JwtProperties jwtProperties;

    @PostMapping("/signup")
    public ResponseEntity<SignupRequest> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);

        return ResponseEntity.ok()
                .body(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        String refreshToken = loginResponse.getRefreshToken();

        ResponseCookie responseCookie =
                cookieUtils.generateCookie(REFRESH_TOKEN, refreshToken, jwtProperties.getRefreshTokenExpiration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
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
