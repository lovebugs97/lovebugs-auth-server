package com.lovebugs.auth.dto.admin;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import com.lovebugs.auth.domain.enums.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AdminDto {
    @Getter
    @RequiredArgsConstructor
    public static class MemberListResponse {
        private final Integer id;
        private final String name;
        private final String email;
        private final String profileImage;
        private final LocalDateTime createdAt;
        private final LocalDateTime lastLoginDate;
        private final Gender gender;
        private final String refreshToken;
        private final RoleType roleType;

        public MemberListResponse(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.email = member.getEmail();
            this.profileImage = member.getProfileImage();
            this.createdAt = member.getCreatedAt();
            this.lastLoginDate = member.getLastLoginDate();
            this.gender = member.getGender();
            this.refreshToken = member.getRefreshToken();
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
