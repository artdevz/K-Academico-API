package com.kacademic.infra.security;

import java.io.IOException;
import java.util.Collections;

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
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtProvider jwtTokenProvider;

    public JwtFilter(JwtProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("[infra.security.JwtTokenFilter]: Interceptando requisição para: {} | Método: {}", request.getRequestURI(), request.getMethod());
        log.debug("[infra.security.JwtTokenFilter]: Cabeçalhos recebidos: {}", Collections.list(request.getHeaderNames()));
        System.out.println("[infra.security.JwtTokenFilter]: Interceptando requisição para: " + request.getRequestURI() + " | Método: " + request.getMethod());
        System.out.println("[infra.security.JwtTokenFilter]: Cabeçalhos recebidos: " + Collections.list(request.getHeaderNames()));

        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("[infra.security.JwtTokenFilter]: Token recebido no filtro: " + token);

        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {

                Authentication auth = jwtTokenProvider.getAuthentication(token);

                if (auth != null) {
                    log.debug("[infra.security.JwtTokenFilter]: Usuário autenticado: {}", auth.getName());
                    System.out.println("[infra.security.JwtTokenFilter]: Usuário autenticado: " + auth.getName());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    logger.warn("[infra.security.JwtTokenFilter]: Falha ao obter autenticação do token");
                    System.out.println("[infra.security.JwtTokenFilter]: Falha ao obter autenticação do token");
                }
            } else {
                log.warn("[infra.security.JwtTokenFilter]: Token inválido ou expirado");
                System.out.println("[infra.security.JwtTokenFilter]: Token inválido ou expirado");
            }
        } else {
            log.debug("[infra.security.JwtTokenFilter]: Nenhum token encontrado no cabeçalho Authorization");
            System.out.println("[infra.security.JwtTokenFilter]: Nenhum token encontrado no cabeçalho Authorization");
        }

        filterChain.doFilter(request, response);
        
        /*
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) SecurityContextHolder.getContext().setAuthentication(auth);
        } */

    }

}