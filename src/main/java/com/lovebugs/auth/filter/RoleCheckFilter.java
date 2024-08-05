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
public class RoleCheckFilter extends OncePerRequestFilter {
    private final String ADMIN_ENDPOINT = "/admin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String roleHeader = request.getHeader("role");

        if (path.startsWith(ADMIN_ENDPOINT)) {
            if (roleHeader == null || !roleHeader.equals(RoleType.ROLE_ADMIN.getRole())) {
                log.warn("CAN NOT Access to {}, Current Role is {}", path, roleHeader);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
