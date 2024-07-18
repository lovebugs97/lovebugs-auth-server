package com.lovebugs.auth.dto;

import com.lovebugs.auth.domain.entity.Member;

import java.util.List;

public record SignupRequest(
        String name,
        String email,
        String profileImage,
        String password,
        Integer gender
) {
    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .profileImage(this.profileImage)
                .password(encodedPassword)
                .roles(roles)
                .build();
    }
}
