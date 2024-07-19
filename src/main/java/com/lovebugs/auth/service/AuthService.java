package com.lovebugs.auth.service;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.RoleType;
import com.lovebugs.auth.dto.LoginRequest;
import com.lovebugs.auth.dto.LoginResponse;
import com.lovebugs.auth.dto.SignupRequest;
import com.lovebugs.auth.dto.TokenDto;
import com.lovebugs.auth.exception.AuthenticationFailedException;
import com.lovebugs.auth.exception.EmailDuplicatedException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.InvalidCredentialException;
import com.lovebugs.auth.repository.MemberRepository;
import com.lovebugs.auth.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        // Email 중복 체크
        if (existsBy(signupRequest.email())) {
            throw new EmailDuplicatedException(ErrorCode.EMAIL_DUPLICATION);
        }

        // Todo: Email 인증 로직 추가 (별도의 메서드로 이메일 인증 로직 구현)

        // 엔티티 생성 (비밀번호 암호화, 권한 부여) & 영속화
        String encodedPassword = bCryptPasswordEncoder.encode(signupRequest.password());
        List<String> roles = List.of(RoleType.USER.getRole());
        Member member = signupRequest.toEntity(encodedPassword, roles);
        memberRepository.save(member);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidCredentialException(ErrorCode.INVALID_CREDENTIAL));

        // Todo: 비밀번호 틀렸을 시 로직 추가, 3회 에러 시 초기화 로직
        if (!passwordMatch(loginRequest.password(), member.getPassword())) {
            throw new InvalidCredentialException(ErrorCode.INVALID_CREDENTIAL);
        }

        // 미인증 상태의 Authentication 객체 생성 (인증 여부를 확인할 때 사용되는 authenticated 값 false)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        // 실제 검증, authenticate() -> loadUserByUsername()
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationFailedException(ErrorCode.AUTHENTICATION_FAIL);
        }

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
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

    private boolean passwordMatch(String rawPassword, String hashedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, hashedPassword);
    }
}