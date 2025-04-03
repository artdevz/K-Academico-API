package com.kacademic.infra.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.kacademic.infra.security.JwtFilter;

// import jakarta.annotation.PostConstruct;

import com.kacademic.infra.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtFilter jwtF;
    private final CorsFilter corsF;
    private final CustomUserDetailsService userDetailsS;

    public SecurityConfig(JwtFilter jwtF, CorsFilter corsF, CustomUserDetailsService userDetailsS) {
        this.jwtF = jwtF;
        this.corsF = corsF;
        this.userDetailsS = userDetailsS;

        log.info("[infra.configs.SecurityConfig]: SecurityConfig Initialized");
        log.debug("[infra.configs.SecurityConfig]: DEBUG MODE actived for SecurityConfig");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/role", "/init", "/error").permitAll() // DEV MODE
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtF, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsF, JwtFilter.class);

        log.info("[infra.configs.SecurityConfig]: SecurityFilterChain configurado com sucesso!");
        return http.build();

    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsS);
        return authBuilder.build();
    }

    // @PostConstruct
    // void enableSecurityContextPropagation() {
    //     SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    // }

}