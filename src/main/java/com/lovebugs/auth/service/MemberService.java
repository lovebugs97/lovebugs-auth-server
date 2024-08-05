package com.lovebugs.auth.service;

import com.lovebugs.auth.domain.entity.Member;
import com.lovebugs.auth.dto.member.MemberDto;
import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.InvalidFilenameException;
import com.lovebugs.auth.exception.MemberNotFoundException;
import com.lovebugs.auth.feign.StorageFeignClient;
import com.lovebugs.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final StorageFeignClient storageFeignClient;

    @Transactional(readOnly = true)
    public void emailDuplicationCheck(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new EmailDuplicationException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    @Transactional
    public MemberDto.UploadProfileImageResponse uploadProfileImage(String email, MultipartFile profileImage) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        String fullFilename = profileImage.getOriginalFilename();

        if (fullFilename == null || fullFilename.lastIndexOf(".") == -1) {
            throw new InvalidFilenameException(ErrorCode.INVALID_FILENAME);
        }

        int base = fullFilename.lastIndexOf(".");

        String filename = fullFilename.substring(0, base);
        String ext = fullFilename.substring(base + 1);
        String path = email + "/profile/" + filename + "_" + UUID.randomUUID() + "." + ext;

        try {
            log.info("trying to save file on {}", path);
            ResponseEntity<String> uploadResponse = storageFeignClient.uploadProfileImage(path, profileImage);
            String url = uploadResponse.getBody();
            member.updateProfileImage(url);

            return new MemberDto.UploadProfileImageResponse(url);
        } catch (Exception e) {
            throw new RuntimeException("failed to upload profile image", e);
        }
    }
}
