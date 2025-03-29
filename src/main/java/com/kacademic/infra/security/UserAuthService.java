package com.kacademic.infra.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.UserRepository;

@Service
public class UserAuthService implements UserDetailsService {

    private final UserRepository userR;

    public UserAuthService(UserRepository userR) {
        this.userR = userR;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userR.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());

        System.out.println("Authorities atribuidas ao usuário (UserAuthS): " + authorities);

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities
        );
        
    }
    
}
