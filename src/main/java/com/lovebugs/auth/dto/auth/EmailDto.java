package com.lovebugs.auth.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class EmailDto {
    @Getter
    @RequiredArgsConstructor
    public static class SendVerificationCodeRequest {
        @Email(regexp = "^[a-zA-Z0-9.]{3,}@[a-zA-Z0-9.]{3,}\\.[a-zA-Z]{2,}$")
        private final String email;

        @Size(min = 2, max = 30, message = "name 필드는 최소 2자 이상 30자 이하여야 합니다.")
        private final String name;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class VerifyCodeRequest {
        @NotNull
        private String verificationCode;
    }
}
