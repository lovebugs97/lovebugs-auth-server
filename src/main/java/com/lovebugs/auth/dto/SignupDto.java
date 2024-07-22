package com.lovebugs.auth.dto;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class SignupDto {
    @Getter
    @RequiredArgsConstructor
    public static class Request {
        private final String name;
        private final String email;
        private final String profileImage;
        private final String password;
        private final Gender gender;

        public Member toEntity(String encodedPassword, List<String> roles) {
            return Member.builder()
                    .name(this.name)
                    .email(this.email)
                    .gender(this.gender)
                    .profileImage(this.profileImage)
                    .password(encodedPassword)
                    .roles(roles)
                    .build();
        }
    }
}
