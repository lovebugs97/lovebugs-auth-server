package com.lovebugs.auth;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@NoArgsConstructor
@Getter @Setter
@Component
public class MessageDto {
    private String test;

    @Override
    public String toString() {
        return "test: " + this.test;
    }

    /* MessageListnerAdapter에 지정된 메세지 도착시 실행되는 메서드 */
    public void receiveMessage(String message) throws ExecutionException, InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<String> future = executorService.schedule(() -> {
            System.out.println(message);
            return message;
        }, 3, TimeUnit.SECONDS);

        System.out.println("Before");
        System.out.println(future.get());
        System.out.println("After");
    }
}
