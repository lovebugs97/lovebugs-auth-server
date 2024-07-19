package com.lovebugs.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class LoginResponse {
    private final Integer id;
    private final String name;
    private final String email;
    private final String profileImage;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLoginDate;
    private final Gender gender;
    private final List<String> roles;

    /* 토큰은 쿠키로 전달 */
    @JsonIgnore
    private final String accessToken;

    @JsonIgnore
    private final String refreshToken;

    public LoginResponse(Member member, String accessToken, String refreshToken) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.profileImage = member.getProfileImage();
        this.createdAt = member.getCreatedAt();
        this.lastLoginDate = member.getLastLoginDate();
        this.gender = member.getGender();
        this.roles = member.getRoles();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
