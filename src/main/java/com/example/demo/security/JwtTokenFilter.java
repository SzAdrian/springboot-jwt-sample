package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// Our custom filter that validated the JWT tokens.
public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // this is called for every request that comes in (unless its filtered out before in the chain)
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.getTokenFromHeader((HttpServletRequest) req);
        // If we have a token validate it.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.loadUserFromTokenInfo(token);
            if (auth != null) {
                // marks the user as authenticated.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // process the next filter.
        filterChain.doFilter(req, res);
    }

}
