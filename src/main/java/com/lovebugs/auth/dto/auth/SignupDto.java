package com.lovebugs.auth.dto.auth;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class SignupDto {
    @Getter
    @RequiredArgsConstructor
    public static class Request {
        @Size(min = 2, max = 30, message = "name 필드는 최소 2자 이상 30자 이하여야 합니다.")
        private final String name;

        @Email(regexp = "^[a-zA-Z0-9.]{3,}@[a-zA-Z0-9.]{3,}\\.[a-zA-Z]{2,}$")
        private final String email;

        @Pattern(regexp = "^(?=.*[!@#$%^&*()>+{}?~])[a-zA-Z0-9!@#$%^&*()_+{}?~]{8,15}$")
        private final String password;

        private final Gender gender;

        public Member toEntity(String encodedPassword, List<String> roles) {
            return Member.builder()
                    .name(this.name)
                    .email(this.email)
                    .gender(this.gender)
                    .password(encodedPassword)
                    .roles(roles)
                    .build();
        }
    }
}
