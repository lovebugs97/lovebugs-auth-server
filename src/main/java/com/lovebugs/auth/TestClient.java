package com.lovebugs.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("config-service")
public interface TestClient {
    @GetMapping("/api/v1/test")
    String getTest();

    @PostMapping("/api/v1/test")
    String postTest();
}
