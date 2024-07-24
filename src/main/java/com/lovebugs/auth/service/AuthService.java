package com.lovebugs.auth.service;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.RoleType;
import com.lovebugs.auth.dto.auth.LoginDto;
import com.lovebugs.auth.dto.auth.LogoutDto;
import com.lovebugs.auth.dto.auth.SignupDto;
import com.lovebugs.auth.dto.auth.TokenDto;
import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.MemberNotFoundException;
import com.lovebugs.auth.repository.MemberRepository;
import com.lovebugs.auth.utils.JwtUtils;
import com.lovebugs.auth.utils.TokenBlackListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenBlackListUtils tokenBlackListUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signup(SignupDto.Request signupRequest) {
        // Email 중복 체크
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailDuplicationException(ErrorCode.EMAIL_DUPLICATION);
        }

        // Todo: Email 인증 로직 추가 (별도의 메서드로 이메일 인증 로직 구현)

        // 엔티티 생성 (비밀번호 암호화, 권한 부여) & 영속화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        List<String> roles = List.of(RoleType.ROLE_USER.getRole());
        Member member = signupRequest.toEntity(encodedPassword, roles);
        memberRepository.save(member);
    }

    @Transactional
    public LoginDto.Response login(LoginDto.Request loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        TokenDto tokenDto = jwtUtils.generateToken(authentication);

        Member member = (Member) authentication.getPrincipal();

        LoginDto.Response loginResponse =
                new LoginDto.Response(member, tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        member.updateLastLoginDate();
        member.updateRefreshToken(tokenDto.getRefreshToken());

        return loginResponse;
    }

    @Transactional
    public void logout(LogoutDto.Request logoutRequest) {
        Integer memberId = logoutRequest.getId();
        String accessToken = logoutRequest.getAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // AccessToken 블랙리스트 처리 (redis 연동), RefreshToken은 단순히 DB에 null 처리만 반영
        Date accessTokenExpiration = jwtUtils.extractExpiration(accessToken);
        if (accessTokenExpiration != null) {
            tokenBlackListUtils.addToBlackList(accessToken, accessTokenExpiration);
        }

        member.updateRefreshToken(null);
    }
}
