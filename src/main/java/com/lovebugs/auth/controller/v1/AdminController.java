package com.lovebugs.auth.controller.v1;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1")
@Slf4j
public class AdminController {
    private final MemberRepository memberRepository;

    @GetMapping("/test")
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 블랙리스트 토큰 현황

    // 회원가입된 유저 현황

    // 온라인 유저 현황

    // 회원 등급 조절 기능

}
