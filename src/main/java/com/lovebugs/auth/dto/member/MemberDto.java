package com.lovebugs.auth.dto.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MemberDto {

    @Getter
    @RequiredArgsConstructor
    public static class UploadProfileImageResponse {
        private final String imageUrl;
    }
}
