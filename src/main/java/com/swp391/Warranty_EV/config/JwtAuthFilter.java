package com.swp391.Warranty_EV.config;

import com.swp391.Warranty_EV.service.AuthenticationService;
import com.swp391.Warranty_EV.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationService userDetailsService;

    // Tiêm theo TÊN để tránh trùng bean (errorAttributes / handlerExceptionResolver)
    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    public JwtAuthFilter(TokenService tokenService,
                         AuthenticationService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /** Bỏ qua public endpoints + preflight OPTIONS */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String p = request.getServletPath();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || p.startsWith("/api/auth/")
                || p.startsWith("/v3/api-docs")
                || p.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);
            String username = tokenService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (tokenService.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            chain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
