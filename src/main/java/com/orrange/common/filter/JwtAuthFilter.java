package com.orrange.common.filter;

import com.orrange.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Set<String> WHITELIST = new HashSet<>(Arrays.asList(
            "/api/user/verification",
            "/api/user/register",
            "/api/user/login",
            "/api/admin/verification",
            "/api/admin/register",
            "/api/admin/login",
            "/error"
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (isWhitelisted(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权\",\"data\":null}");
            return;
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = JwtUtils.parseToken(token);
            Object userId = claims.get("userId");
            request.setAttribute("CURRENT_USER_ID", userId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"无效或过期的令牌\",\"data\":null}");
        }
    }

    private boolean isWhitelisted(String path) {
        if (WHITELIST.contains(path)) return true;
        // 允许静态资源
        return path.startsWith("/static/") || path.startsWith("/public/");
    }
}
