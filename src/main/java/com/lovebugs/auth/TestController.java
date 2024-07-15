package com.lovebugs.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestClient client;

    @GetMapping("/api/v1/auth/test")
    public String getTest() {
        return client.getTest();
    }

    @PostMapping("/api/v1/auth/test")
    public String postTest() {
        return client.postTest();
    }
}
