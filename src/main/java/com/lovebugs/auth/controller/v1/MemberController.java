package com.lovebugs.auth.controller.v1;

import com.lovebugs.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/v1")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 마이페이지에 들어갈 기능들 구현...
    // 1. 프로필 사진 업데이트 기능

}
