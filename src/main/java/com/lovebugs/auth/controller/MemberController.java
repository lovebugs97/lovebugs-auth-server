package com.lovebugs.auth.controller;

import com.lovebugs.auth.dto.member.MemberDto;
import com.lovebugs.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping(value = "/upload/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> uploadProfileImage(
            @RequestHeader("email") String email,
            @RequestPart("profileImage") MultipartFile profileImage
    ) {
        MemberDto.UploadProfileImageResponse res = memberService.uploadProfileImage(email, profileImage);
        return ResponseEntity.ok(res);
    }
}
