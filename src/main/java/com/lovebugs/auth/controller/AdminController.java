package com.lovebugs.auth.controller;

import com.lovebugs.auth.dto.admin.AdminDto;
import com.lovebugs.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    // 회원가입된 유저 현황
    @GetMapping("/member/list")
    public ResponseEntity<Page<AdminDto.MemberListResponse>> FindAllMembers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<AdminDto.MemberListResponse> members = adminService.findMembers(page, size);
        return ResponseEntity.ok(members);
    }

    // 블랙리스트 토큰 현황
    @GetMapping("/member/token/blacklisted")
    public ResponseEntity<AdminDto.BlackListResponse> findBlackListedTokens() {
        AdminDto.BlackListResponse tokens = adminService.getBlackListedTokens();
        return ResponseEntity.ok(tokens);
    }

    // 회원 등급 조절 기능

    // 온라인 유저 현황
}
