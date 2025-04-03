package com.kacademic.infra.security;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userR;

    public CustomUserDetailsService(UserRepository userR) {
        this.userR = userR;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userR.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username with email " + username + "not found"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
        System.out.println("ROLE_ Adicionado em infra.security.CustomUserDetailsService");

        if(log.isDebugEnabled()) log.debug("[infra.security.CustomUserDetailsService]: Authorities atribuitas ao Usuário '{}': {}", username, authorities);

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities
        );
        
    }
    
}