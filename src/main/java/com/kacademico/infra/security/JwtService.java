package com.kacademico.infra.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.domain.models.User;
import com.kacademico.domain.repositories.UserRepository;
import com.kacademico.infra.configs.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtService {
    
    private final JwtConfig jwtConfig;
    private final UserRepository userR;

    public String generateAccessToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("name", user.getName());
        extraClaims.put("roles", user.getRoles().stream().map(role -> role.getName()).toList());

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + JwtConfig.EXPIRATION_TIME_MS))
            .setHeaderParam("alg", "HS256")
            .signWith(getKey(), JwtConfig.SIGNATURE_ALGORITHM)
            .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
            .setSubject(user.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + JwtConfig.EXPIRATION_TIME_MS * 24 * 30))
            .signWith(getKey(), JwtConfig.SIGNATURE_ALGORITHM)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
                            
            return true;
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        return (bearerToken != null && bearerToken.startsWith("Bearer "))? bearerToken.substring(7) : null; 
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        User userDetails = findUserByEmail(claims.getSubject());
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private User findUserByEmail(String email) {
        return userR.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
    }

    private Key getKey() {
        return new SecretKeySpec(jwtConfig.getSecretKey().getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName());
    }

}