package com.kacademic.infra.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class); // REMOVER
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("Interceptando requisição para: {}", request.getRequestURI());

        String token = jwtTokenProvider.resolveToken(request);

        if (token != null) logger.debug("Token encontrado: {}", token); // Remover
        else logger.debug("Nenhum token encontrado no cabeçalho Authorization");

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                logger.debug("Usuário autenticado: {}", auth.getName());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                logger.warn("Falha ao obter autenticação do token");
            }
        } else {
            logger.warn("Token inválido ou expirado");
        }

        filterChain.doFilter(request, response);

    }

}
