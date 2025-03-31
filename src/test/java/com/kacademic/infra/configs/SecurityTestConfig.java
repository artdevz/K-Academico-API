package com.kacademic.infra.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kacademic.infra.security.JwtTokenFilter;
import com.kacademic.infra.security.JwtTokenProvider;

@TestConfiguration
@EnableWebSecurity
public class SecurityTestConfig {
    
    @Bean
    public JwtTokenFilter jwtF(JwtTokenProvider jwtP) {
        return new JwtTokenFilter(jwtP);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/init/*", "/error").permitAll() // DEV MODE
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtF(null), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
