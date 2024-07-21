package com.lovebugs.auth.service;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.RoleType;
import com.lovebugs.auth.dto.LoginRequest;
import com.lovebugs.auth.dto.LoginResponse;
import com.lovebugs.auth.dto.SignupRequest;
import com.lovebugs.auth.dto.TokenDto;
import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.repository.MemberRepository;
import com.lovebugs.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        // Email 중복 체크
        if (existsBy(signupRequest.email())) {
            throw new EmailDuplicationException(ErrorCode.EMAIL_DUPLICATION);
        }

        // Todo: Email 인증 로직 추가 (별도의 메서드로 이메일 인증 로직 구현)

        // 엔티티 생성 (비밀번호 암호화, 권한 부여) & 영속화
        String encodedPassword = passwordEncoder.encode(signupRequest.password());
        List<String> roles = List.of(RoleType.ROLE_USER.getRole());
        Member member = signupRequest.toEntity(encodedPassword, roles);
        memberRepository.save(member);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        TokenDto tokenDto = jwtUtils.generateToken(authentication);

        Member member = (Member) authentication.getPrincipal();
        member.updateLastLoginDate();
        member.updateRefreshToken(tokenDto.refreshToken());

        return new LoginResponse(member, tokenDto.accessToken(), tokenDto.refreshToken());
    }

    @Transactional(readOnly = true)
    public void logout() {
        // Todo
    }

    private boolean existsBy(String email) {
        return memberRepository.existsByEmail(email);
    }
}
