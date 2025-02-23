package com.kacademic.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kacademic.security.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final AuthenticationProvider authProvider;
    private final JwtTokenFilter jwtF;

    public SecurityConfig(AuthenticationProvider authProvider, JwtTokenFilter jwtF) {
        this.authProvider = authProvider;
        this.jwtF = jwtF;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtF, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
