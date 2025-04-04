package com.kacademic.infra.security;

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
public class JwtProvider {
    
    private final JwtConfig jwtC;
    private final UserRepository userR;

    public JwtProvider(JwtConfig jwtC, UserRepository userR) {
        this.jwtC = jwtC;
        this.userR = userR;
    }

    public String generateToken(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        
        User userPrincipal = userR.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));

        String SECRET_KEY = jwtC.getSecretKey();
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userPrincipal.getId());
        extraClaims.put("name", userPrincipal.getName());
        extraClaims.put("roles", userPrincipal.getRoles().stream().map(role -> role.getName()).toList());

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
            String SECRET_KEY = jwtC.getSecretKey();

            Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(SECRET_KEY.getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName()))
                .build()
                .parseClaimsJws(token);
                
            return true;
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) return bearerToken.substring(7); 
        return null;
    }

    public Authentication getAuthentication(String token) {
        String SECRET_KEY = jwtC.getSecretKey();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(SECRET_KEY.getBytes(), JwtConfig.SIGNATURE_ALGORITHM.getJcaName()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        User userDetails = userR.findByEmail(claims.getSubject())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

}