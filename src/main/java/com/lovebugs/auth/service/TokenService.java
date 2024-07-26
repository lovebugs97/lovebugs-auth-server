package com.lovebugs.auth.service;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.dto.auth.TokenDto;
import com.lovebugs.auth.dto.token.TokenReIssueDto;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.MemberNotFoundException;
import com.lovebugs.auth.exception.TokenInvalidationException;
import com.lovebugs.auth.repository.MemberRepository;
import com.lovebugs.auth.utils.JwtUtils;
import com.lovebugs.auth.utils.TokenBlackListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;
    private final TokenBlackListUtils tokenBlackListUtils;

    public void validateToken(String token) {
        if (token == null || tokenBlackListUtils.isTokenBlackListed(token) || !jwtUtils.validateToken(token)) {
            throw new TokenInvalidationException(ErrorCode.TOKEN_INVALIDATION);
        }
    }

    @Transactional
    public TokenReIssueDto.Response reissueToken(TokenReIssueDto.Request tokenReIssueDto) {
        Member member = memberRepository.findById(tokenReIssueDto.getId())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 저장된 RefreshToken과 일치하지 않는 경우
        if (!member.getRefreshToken().equals(tokenReIssueDto.getRefreshToken())) {
            log.info("Token No Matched");
            log.info("DB RefreshToken: {}", member.getRefreshToken());
            log.info("Request RefreshToken: {}", tokenReIssueDto.getRefreshToken());
            throw new TokenInvalidationException(ErrorCode.TOKEN_INVALIDATION);
        }

        // 이미 RefreshToken도 만료된 경우
        if (!jwtUtils.validateToken(tokenReIssueDto.getRefreshToken())) {
            log.info("Token Expiration: {}", jwtUtils.extractExpiration(tokenReIssueDto.getRefreshToken()));
            throw new TokenInvalidationException(ErrorCode.TOKEN_INVALIDATION);
        }

        // DB에 있는 토큰 정보와 일치하고 아직 유효한 RefreshToken인 경우 재발행
        TokenDto tokenDto = jwtUtils.generateToken(member);
        member.updateRefreshToken(tokenDto.getRefreshToken());

        return TokenReIssueDto.Response.of(tokenDto);
    }
}
