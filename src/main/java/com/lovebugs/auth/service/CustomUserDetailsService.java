package com.lovebugs.auth.service;

import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.InvalidCredentialException;
import com.lovebugs.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /* authenticationManager.authenticate() 메서드 실행시 loadUserByUsername 메서드 실행 */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByEmail: {}", email);

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialException(ErrorCode.INVALID_CREDENTIAL));
    }
}
