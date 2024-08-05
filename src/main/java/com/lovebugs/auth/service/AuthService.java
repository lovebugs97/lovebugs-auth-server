package com.lovebugs.auth.service;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.domain.enums.RoleType;
import com.lovebugs.auth.dto.auth.*;
import com.lovebugs.auth.dto.token.TokenReIssueDto;
import com.lovebugs.auth.exception.*;
import com.lovebugs.auth.repository.MemberRepository;
import com.lovebugs.auth.utils.JwtUtils;
import com.lovebugs.auth.utils.TokenBlackListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenBlackListUtils tokenBlackListUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // TODO: 이메일 발송하고 제한 시간(5분)동안 Redis에 저장
    public void sendVerificationCode(EmailDto.SendVerificationCodeRequest request) {
        log.info("Send Verification Code to {}, {}", request.getEmail(), request.getName());
    }

    // TODO: Redis에 해당 코드가 있는지 검증 -> 있으면 삭제후 정상 응답, 없거나 제한시간 초과시 에러
    public void verifyVerificationCode(EmailDto.VerifyCodeRequest verifyCodeRequest) {
        log.info("Verifying code {}", verifyCodeRequest.getVerificationCode());
    }

    @Transactional
    public void signup(SignupDto.Request signupRequest) {
        // Email 중복 체크 (사전 검증이 되지만 방어 코드로 남겨둠)
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailDuplicationException(ErrorCode.EMAIL_DUPLICATION);
        }

        // 엔티티 생성 (비밀번호 암호화, 권한 부여) & 영속화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        Member member = signupRequest.toEntity(encodedPassword, RoleType.ROLE_ADMIN);
        memberRepository.save(member);
    }

    @Transactional
    public LoginDto.Response login(LoginDto.Request loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new AuthenticationFailureException(ErrorCode.AUTHENTICATION_FAIL);
        }

        TokenDto tokenDto = jwtUtils.generateToken(member.getRoleType(), member.getEmail());

        member.updateLastLoginDate();
        member.updateRefreshToken(tokenDto.getRefreshToken());

        return new LoginDto.Response(member, tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    @Transactional
    public void logout(LogoutDto.Request logoutRequest) {
        Integer memberId = logoutRequest.getId();
        String accessToken = logoutRequest.getAccessToken();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // AccessToken 블랙리스트 처리, RefreshToken 단순 null 처리
        Date accessTokenExpiration = jwtUtils.extractExpiration(accessToken);
        if (accessTokenExpiration != null) {
            tokenBlackListUtils.addToBlackList(accessToken, accessTokenExpiration);
        }

        member.updateRefreshToken(null);
    }

    @Transactional
    public TokenReIssueDto.Response reissueToken(TokenReIssueDto.Request tokenReIssueDto) {
        Member member = memberRepository.findById(tokenReIssueDto.getId())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 이미 RefreshToken도 만료된 경우
        if (!jwtUtils.validateToken(tokenReIssueDto.getRefreshToken())) {
            log.info("Token Expiration: {}", jwtUtils.extractExpiration(tokenReIssueDto.getRefreshToken()));
            throw new TokenInvalidationException(ErrorCode.TOKEN_INVALIDATION);
        }

        // 저장된 RefreshToken과 일치하지 않는 경우
        if (!member.getRefreshToken().equals(tokenReIssueDto.getRefreshToken())) {
            log.info("Token No Matched");
            log.info("DB RefreshToken: {}", member.getRefreshToken());
            log.info("Request RefreshToken: {}", tokenReIssueDto.getRefreshToken());
            throw new TokenInvalidationException(ErrorCode.TOKEN_INVALIDATION);
        }

        TokenDto tokenDto = jwtUtils.generateToken(member.getRoleType(), member.getEmail());
        member.updateRefreshToken(tokenDto.getRefreshToken());

        return TokenReIssueDto.Response.of(tokenDto);
    }
}