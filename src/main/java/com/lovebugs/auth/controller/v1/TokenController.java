package com.lovebugs.auth.controller.v1;

import com.lovebugs.auth.dto.token.TokenReIssueDto;
import com.lovebugs.auth.service.TokenService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token/v1")
public class TokenController {
    private static final String BEARER = "Bearer";
    private final TokenService tokenService;

    @PostMapping("/validation")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token) {
        String parsedToken = token.replace(BEARER, "").trim();
        tokenService.validateToken(parsedToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenReIssueDto.Response> reissueToken(@RequestBody TokenReIssueDto.Request tokenReIssueDto) {
        TokenReIssueDto.Response res = tokenService.reissueToken(tokenReIssueDto);
        return ResponseEntity.ok(res);
    }
}
