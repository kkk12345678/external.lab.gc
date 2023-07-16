package org.example.gc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class JwtTokenFilter extends HttpFilter {
    private final JwtTokenProvider jwtTokenProvider;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken(req);
        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (IllegalStateException e) {
                res.setStatus(403);
            }
        }
        filterChain.doFilter(req, res);
    }
}