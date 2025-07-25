package com.kacademico.infra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.kacademico.infra.security.CustomUserDetailsService;
import com.kacademico.infra.security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtFilter jwtF;
    private final CorsFilter corsF;
    private final CustomUserDetailsService userDetailsS;

    public SecurityConfig(JwtFilter jwtF, CorsFilter corsF, CustomUserDetailsService userDetailsS) {
        this.jwtF = jwtF;
        this.corsF = corsF;
        this.userDetailsS = userDetailsS;
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
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtF, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsF, JwtFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsS);
        
        return authBuilder.build();
    }

}