package com.lovebugs.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("config-service")
public interface TestClient {
    @GetMapping("/")
    String getTest();

    @PostMapping("/")
    String postTest();
}
