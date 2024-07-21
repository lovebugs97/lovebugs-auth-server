package com.lovebugs.auth.dto;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.Gender;

import java.util.List;

public record SignupRequest(
        String name,
        String email,
        String profileImage,
        String password,
        Gender gender
) {
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
