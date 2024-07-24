package com.lovebugs.auth.dto.auth;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

public class LoginDto {
    @Getter
    @RequiredArgsConstructor
    public static class Request {
        private final String email;
        private final String password;
    }

    @Getter
    public static class Response {
        private final Integer id;
        private final String name;
        private final String email;
        private final String profileImage;
        private final LocalDateTime createdAt;
        private final LocalDateTime lastLoginDate;
        private final Gender gender;
        private final String accessToken;
        private final String refreshToken;

        public Response(Member member, String accessToken, String refreshToken) {
            this.id = member.getId();
            this.name = member.getName();
            this.email = member.getEmail();
            this.profileImage = member.getProfileImage();
            this.createdAt = member.getCreatedAt();
            this.lastLoginDate = member.getLastLoginDate();
            this.gender = member.getGender();
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
