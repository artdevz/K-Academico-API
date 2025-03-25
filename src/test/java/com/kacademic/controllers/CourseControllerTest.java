package com.kacademic.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacademic.dto.course.CourseRequestDTO;
import com.kacademic.models.User;
import com.kacademic.security.JwtTokenProvider;
import com.kacademic.services.CourseService;

@WebMvcTest(CourseController.class)
@ExtendWith(SpringExtension.class)
public class CourseControllerTest {

    @MockBean
    AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CourseService courseS;

    @BeforeEach
    void setup() {

        String token = "fake-jwt-token";
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(token)).thenReturn(createAuthentication());
        
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // when(authenticationManager.authenticate(any(Authentication.class)))
        //     .thenReturn(new UsernamePasswordAuthenticationToken("arthur@gmail.com", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        // when(jwtTokenProvider.generateToken(any(Authentication.class)))
        //     .thenReturn("fake-jwt-token");
        
        // String token = "fake-jwt-token";
        // when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        
        // Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        // // Authentication authentication = new UsernamePasswordAuthenticationToken(
        // //     "arthur@gmail.com", null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        // // );
        // SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        User userDetails = new User("Arthur", "arthur@gmail.com", "4bcdefg!");
        userDetails.setAuthorities(authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    @Test
    @DisplayName("Given an invalid course name, when creating a course, then should return Status 422 with validation error")
    void givenInvalidCourseName_whenCreateCourse_thenReturnsUnprocessableEntity() throws Exception {

        // Given
        CourseRequestDTO data = new CourseRequestDTO("Go", "12", "Dor e Sofrimento");
        
        // When
        when(courseS.createAsync(data)).thenThrow(new IllegalArgumentException("Course name must be between 4 and 160 characters"));

        mockMvc.perform(post("/api/course")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateValidJwtToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(data)))
        
        // Then
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("Course name must be between 4 and 160 characters"));

    }

    private String generateValidJwtToken() {
        System.out.println("Válido? " + jwtTokenProvider.validateToken("fake-jwt-token"));
        return "fake-jwt-token";
    }

}
