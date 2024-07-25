package com.lovebugs.auth.service;

import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public void findMemberByEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailDuplicationException(ErrorCode.EMAIL_DUPLICATION);
        }
    }
}
