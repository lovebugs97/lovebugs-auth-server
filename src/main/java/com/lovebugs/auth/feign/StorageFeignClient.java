package com.lovebugs.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient("STORAGE-SERVER")
public interface StorageFeignClient {
    @PostMapping(path = "/storage/upload/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ResponseEntity<String> uploadProfileImage(@RequestPart("path") String path, @RequestPart("profileImage") MultipartFile profileImage);
}
