package com.example.main.security;

import com.example.main.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailService customUserDetailService;

    public JwtFilter(JwtUtils jwtUtils, CustomUserDetailService customUserDetailService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailService = customUserDetailService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (jwt != null && jwt.startsWith("Bearer ")) {
            token = jwt.substring(7);
            username = jwtUtils.extractUsername(token);

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            if (jwtUtils.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }
}
