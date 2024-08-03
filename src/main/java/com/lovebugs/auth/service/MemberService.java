package com.lovebugs.auth.service;

import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void emailDuplicationCheck(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailDuplicationException(ErrorCode.EMAIL_DUPLICATION);
        }
    }
}
