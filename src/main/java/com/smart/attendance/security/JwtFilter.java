package com.smart.attendance.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends org.springframework.web.filter.OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // 1️⃣ Check header
        if (header != null && header.startsWith("Bearer ")) {

            // 2️⃣ Token nikala
            String token = header.substring(7);

            // 3️⃣ Token valid hai ya nahi
            if (jwtUtil.isTokenValid(token)) {

                // 4️⃣ Token se data nikala
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                // 5️⃣ Spring Security authentication set
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                jwtUtil.getAuthorities(role)
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);
            }
        }

        // 6️⃣ Request ko aage bhejo
        filterChain.doFilter(request, response);
    }
    
}
