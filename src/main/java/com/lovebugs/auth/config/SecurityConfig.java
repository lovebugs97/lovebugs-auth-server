package com.lovebugs.auth.config;

import com.lovebugs.auth.filter.AccessTokenBlackListCheckFilter;
import com.lovebugs.auth.filter.RoleCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final RoleCheckFilter roleCheckFilter;
    private final AccessTokenBlackListCheckFilter accessTokenBlackListCheckFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .cors(CorsConfigurer::disable)      /* Gateway에서 수행 */
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .addFilterBefore(roleCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accessTokenBlackListCheckFilter, RoleCheckFilter.class)
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        /* default: BCrypt */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
