package com.dzikriananda.multimatic_backend.filter;

import com.dzikriananda.multimatic_backend.interfaces.AuthService;
import com.dzikriananda.multimatic_backend.model.User;
import com.dzikriananda.multimatic_backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthService authService;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String userIp = request.getHeader("X-Forwarded-For");
        if (userIp == null || userIp.isEmpty() || "unknown".equalsIgnoreCase(userIp)) {
            userIp = request.getRemoteAddr();
        }
        logger.info("IP" + userIp + "Trying to access endpoint " + request.getRequestURI());
        logger.info(authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            logger.info("Bearer not found");
            return;
        }
        try {
            final String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info(userEmail + "Has Accessed Endpoint" + request.getRequestURI());

            if (userEmail != null && authentication == null) {
                User user = authService.findUserByEmail(userEmail);
                if (jwtService.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authenticationCredential =
                            new UsernamePasswordAuthenticationToken(userEmail,null, new ArrayList<>());
                    authenticationCredential.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationCredential);
                    filterChain.doFilter(request, response);
                }
            } else {
                logger.info(authentication.getName());
            }
        } catch(Exception e) {
            logger.info("error" + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
