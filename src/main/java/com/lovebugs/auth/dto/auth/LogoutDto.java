package com.lovebugs.auth.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class LogoutDto {
    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        @Positive
        private Integer id;

        @NotNull
        private String accessToken;
    }
}
