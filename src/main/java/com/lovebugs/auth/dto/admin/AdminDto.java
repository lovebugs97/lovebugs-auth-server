package com.lovebugs.auth.dto.admin;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import com.lovebugs.auth.domain.enums.RoleType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
            this.roleType = member.getRoleType();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class AddBlackListObject implements Serializable {
        private final Date addedAt;
        private final Date expAt;
        private final String token;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlackListTokenInfo {
        private String token;
        private String email;
        private String roleType;
        private Date addedAt;
        private Date expAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class BlackListResponse {
        private final Integer count;
        private final List<BlackListTokenInfo> tokenInfos;
    }
}
