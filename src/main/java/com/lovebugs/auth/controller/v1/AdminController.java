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
}
