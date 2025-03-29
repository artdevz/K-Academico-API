package com.kacademic.infra.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.UserRepository;
import com.kacademic.infra.configs.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final JwtConfig jwtConfig;

    private final UserRepository userR;

    public JwtTokenProvider(JwtConfig jwtConfig, UserRepository userR) {
        this.jwtConfig = jwtConfig;
        this.userR = userR;
    }

    public String generateToken(Authentication auth) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        User userPrincipal = userR.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));

        String SECRET_KEY = jwtConfig.getSecretKey();
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userPrincipal.getId());
        extraClaims.put("name", userPrincipal.getName());
        extraClaims.put("roles", userPrincipal.getRoles().stream()
            .map(role -> role.getName())
            .toList());

        logger.debug("Gerando token para usuário: {}", userPrincipal.getEmail());

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userPrincipal.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + JwtConfig.EXPIRATION_TIME_MS))
            .setHeaderParam("alg", "HS256")
            .signWith(key, JwtConfig.SIGNATURE_ALGORITHM)
            .compact();

    }

    public boolean validateToken(String token) {

        try {
            String SECRET_KEY = jwtConfig.getSecretKey();

            Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(SECRET_KEY.getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName()))
                .build()
                .parseClaimsJws(token);
            logger.debug("Token válido");
            return true;
        }

        catch (JwtException | IllegalArgumentException e) {
            logger.warn("Token inválido ou expirado: {}", e.getMessage());
            return false;
        }

    }

    public String resolveToken(HttpServletRequest request) {
        
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            logger.debug("Token extraído do cabeçalho: {}", bearerToken.substring(7));
            return bearerToken.substring(7); 
        }
        
        logger.debug("Token não encontrado no cabeçalho Authorization");
        return null;

    }

    public Authentication getAuthentication(String token) {

        String SECRET_KEY = jwtConfig.getSecretKey();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(SECRET_KEY.getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        System.out.println("Token Claims: " + claims);

        User userDetails = userR.findByEmail(claims.getSubject())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));

        // List<GrantedAuthority> authorities = userDetails.getRoles().stream()
        //     .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
        //     .collect(Collectors.toList());

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

        System.out.println("USUÁRIO AUTENTICADO: " + claims.getSubject());
        System.out.println("AUTHORITIES ATRIBUIDAS AO USUÁRIO: " + userDetails.getAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Authorities do SecurityContext: " + authentication.getAuthorities());
        System.out.println("Autoridades no SecurityContext: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return authentication;
        // return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    
    }

}
