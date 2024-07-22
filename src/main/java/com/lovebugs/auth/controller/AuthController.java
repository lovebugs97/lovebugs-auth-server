package com.lovebugs.auth.controller;

import com.lovebugs.auth.config.JwtProperties;
import com.lovebugs.auth.dto.LoginDto;
import com.lovebugs.auth.dto.SignupDto;
import com.lovebugs.auth.service.AuthService;
import com.lovebugs.auth.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
@Slf4j
public class AuthController {
    private static final String REFRESH_TOKEN = "refreshToken";
    private final AuthService authService;
    private final CookieUtils cookieUtils;
    private final JwtProperties jwtProperties;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupDto.Request signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto.Response> login(@RequestBody LoginDto.Request loginRequest) {
        LoginDto.Response loginResponse = authService.login(loginRequest);
        String refreshToken = loginResponse.getRefreshToken();

        ResponseCookie responseCookie =
                cookieUtils.generateCookie(REFRESH_TOKEN, refreshToken, jwtProperties.getRefreshTokenExpiration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(REFRESH_TOKEN) String refreshToken) {
        System.out.println(refreshToken);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
