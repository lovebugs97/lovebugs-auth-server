package com.lovebugs.auth.service;

import com.lovebugs.auth.dto.admin.AdminDto;
import com.lovebugs.auth.repository.MemberRepository;
import com.lovebugs.auth.utils.TokenBlackListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final TokenBlackListUtils tokenBlackListUtils;

    public Page<AdminDto.MemberListResponse> findMembers(int page, int size) {
        log.info("find members, page: {}, size: {}", page, size);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return memberRepository.findAll(pageRequest).map(AdminDto.MemberListResponse::new);
    }

    public AdminDto.BlackListResponse getBlackListedTokens() {
        List<AdminDto.BlackListTokenInfo> tokenInfos = tokenBlackListUtils.getBlackListedTokens();
        return new AdminDto.BlackListResponse(tokenInfos.size(), tokenInfos);
    }
}
