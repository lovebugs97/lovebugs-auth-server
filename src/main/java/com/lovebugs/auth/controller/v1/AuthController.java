package com.lovebugs.auth.controller.v1;

import com.lovebugs.auth.config.JwtProperties;
import com.lovebugs.auth.dto.auth.LoginDto;
import com.lovebugs.auth.dto.auth.LogoutDto;
import com.lovebugs.auth.dto.auth.SignupDto;
import com.lovebugs.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * RefreshToken
 * - 쿠키 정책으로 인해 FrontEnd와 BackEnd의 도메인이 같아야 함
 * - 따라서, 개발 단계에서는 응답으로 FrontEnd에 전달한 뒤 암호화
 * - 배포 단계에서는 도메인 구입 후 쿠키 방식으로 변경 -> api v2에서 업그레이드 예정
 * - (FrontEnd Domain: example.com, BackEnd Domain: api.example.com) => 상위 도메인 일치해야 함
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
@Validated
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupDto.Request signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto.Response> login(@RequestBody @Valid LoginDto.Request loginRequest) {
        LoginDto.Response loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutDto.Request logoutRequest) {
        String accessToken = logoutRequest.getAccessToken().replace(jwtProperties.getPrefix(), "").trim();
        logoutRequest.setAccessToken(accessToken);
        authService.logout(logoutRequest);
        return ResponseEntity.ok().build();
    }
}
