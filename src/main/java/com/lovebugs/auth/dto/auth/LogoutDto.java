package com.lovebugs.auth.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LogoutDto {
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private Integer id;
        private String accessToken;
    }
}
