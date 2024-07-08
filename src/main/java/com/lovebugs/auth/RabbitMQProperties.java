package com.lovebugs.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
@RequiredArgsConstructor
@Getter
public class RabbitMQProperties {
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    /* Spring Boot 3.x
     * - @ConstructorBinding이 생성자와 애노테이션에만 붙일 수 있게 변경
     * - 단일 생성자인 경우 @ConstructorBinding 애노테이션이 없어도 단일 생성자를 통해 주입
     * - 따라서, @ConstructorBinding을 생략하고 Lombok의 @RequiredArgsConstructor만 사용하는 것이 더 간결함.
    @ConstructorBinding
    public RabbitMQProperties(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
    */
}
