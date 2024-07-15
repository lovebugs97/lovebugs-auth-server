package com.lovebugs.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RabbitMQController {
    private final RabbitMQService rabbitMQService;

    @PostMapping("/test/send/message")
    public ResponseEntity<String> sendMessage(
            @RequestBody MessageDto messageDto
    ) {
        this.rabbitMQService.sendMessage(messageDto);
        return ResponseEntity.ok("Message Sent to RabbitMQ");
    }

    @GetMapping("/test/send/message/{email}")
    public ResponseEntity<String> sendMessage(
            @PathVariable("email") String email
    ) {
        MessageDto dto = new MessageDto();
        dto.setTest(email);
        this.rabbitMQService.sendMessage(dto);
        return ResponseEntity.ok("Message Sent to RabbitMQ");
    }
}
