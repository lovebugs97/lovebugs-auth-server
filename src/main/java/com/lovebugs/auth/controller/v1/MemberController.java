package com.lovebugs.auth.controller.v1;

import com.lovebugs.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member/v1")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check/email/{email}")
    public ResponseEntity<Void> checkEmail(@PathVariable("email") String email) {
        memberService.findMemberByEmail(email);
        return ResponseEntity.ok().build();
    }
}
