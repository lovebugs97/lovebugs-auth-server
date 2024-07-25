package com.lovebugs.auth.dto.auth;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import com.lovebugs.auth.domain.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
            this.roleType = findRoleType(member.getRoles());
        }

        // 가장 높은 등급 1개 반환
        private RoleType findRoleType(List<String> roles) {
            return roles.stream()
                    .map(RoleType::fromRoleString)
                    .filter(Objects::nonNull)
                    .max(Comparator.comparingInt((RoleType::ordinal)))
                    .orElse(null);
        }
    }
}
