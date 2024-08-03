package com.lovebugs.auth.filter;

import com.lovebugs.auth.utils.TokenBlackListUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccessTokenBlackListCheckFilter extends OncePerRequestFilter {
    private final TokenBlackListUtils tokenBlackListUtils;
    private final List<String> NO_CHECK_URL = List.of("/auth");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        String path = request.getRequestURI();
        boolean noCheck = NO_CHECK_URL.stream().anyMatch(path::startsWith);

        if (!noCheck && (token == null || tokenBlackListUtils.isTokenBlackListed(token))) {
            log.warn("Token {} is Null or BlackListed", token);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }
}
