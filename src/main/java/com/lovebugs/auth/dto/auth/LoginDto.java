package com.lovebugs.auth.dto.auth;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import com.lovebugs.auth.domain.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

public class LoginDto {
    @Getter
    @RequiredArgsConstructor
    public static class Request {
        @Email(regexp = "^[a-zA-Z0-9.]{3,}@[a-zA-Z0-9.]{3,}\\.[a-zA-Z]{2,}$")
        private final String email;

        @Pattern(regexp = "^(?=.*[!@#$%^&*()>+{}?~])[a-zA-Z0-9!@#$%^&*()_+{}?~]{8,15}$")
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
        private final RoleType roleType;

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
            this.roleType = member.getRoleType();
        }
    }
}
