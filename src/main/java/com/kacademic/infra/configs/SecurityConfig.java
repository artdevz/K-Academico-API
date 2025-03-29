package com.kacademic.infra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.kacademic.infra.security.JwtTokenFilter;
import com.kacademic.infra.security.UserAuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final JwtTokenFilter jwtF;
    private final CorsFilter corsF;
    private final UserAuthService userAuthS;

    public SecurityConfig(JwtTokenFilter jwtF, CorsFilter corsF, UserAuthService userAuthS) {
        this.jwtF = jwtF;
        this.corsF = corsF;
        this.userAuthS = userAuthS;
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
                // .anyRequest().authenticated()
            )
            .addFilterBefore(jwtF, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsF, JwtTokenFilter.class);
        System.out.println("SecurityFilterChain carregado corretamente!");
        return http.build();

    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userAuthS);

        return authBuilder.build();

    }

}
