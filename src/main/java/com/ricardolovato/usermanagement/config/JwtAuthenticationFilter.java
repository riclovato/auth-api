package com.ricardolovato.usermanagement.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ricardolovato.usermanagement.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

   @Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    String method = request.getMethod();

    return path.startsWith("/h2-console")
            || ("POST".equals(method) && path.startsWith("/api/v1/users"))
            || ("POST".equals(method) && path.startsWith("/api/v1/auth/login"));
}
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String subject = jwtService.extractSubject(token);

        String role = jwtService.extractRole(token); // "USER" ou "ADMIN"

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        var authentication = new UsernamePasswordAuthenticationToken(
                subject,
                null,
                authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
