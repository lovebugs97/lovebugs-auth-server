package com.lovebugs.auth.filter;

import com.lovebugs.auth.domain.enums.RoleType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthorityCheckFilter extends OncePerRequestFilter {
    private final String ADMIN_ENDPOINT = "/admin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String authorityHeader = request.getHeader("authorities");

        if (path.startsWith(ADMIN_ENDPOINT)) {
            if (authorityHeader == null || !authorityHeader.equals(RoleType.ROLE_ADMIN.getRole())) {
                log.warn("No Access Authority: {}", authorityHeader);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
