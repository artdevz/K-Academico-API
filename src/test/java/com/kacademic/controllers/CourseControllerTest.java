package com.kacademic.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacademic.app.dto.auth.AuthRequestDTO;
import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.services.AuthService;
import com.kacademic.app.services.CourseService;
import com.kacademic.app.services.UserService;
import com.kacademic.infra.security.JwtTokenProvider;

public class CourseControllerTest {

    @MockBean
    AuthenticationManager authenticationManager;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CourseService courseS;

    @MockBean 
    UserService userS;

    @MockBean
    AuthService authS;

    @BeforeEach
    void setup() {

        when(userS.createUserTesterSync()).thenReturn("Created User Tester");

        when(jwtTokenProvider.validateToken(any(String.class))).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(any(String.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(
                "test@gmail.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            ));
        
    }

    @Test
    @DisplayName("Given an invalid course name, when creating a course, then should return Status 422 with validation error")
    void givenInvalidCourseName_whenCreateCourse_thenReturnsUnprocessableEntity() throws Exception {

        System.out.println("CreaterUserTester: " + userS.createUserTesterSync()); // Email: test@gmail.com Password: 4bcdefg!test

        // Given
        userS.createUserTesterSync();
        CourseRequestDTO data = new CourseRequestDTO("Go", "12", "Dor e Sofrimento");
        
        // When
        when(courseS.createAsync(data)).thenThrow(new IllegalArgumentException("Course name must be between 4 and 160 characters"));

        CompletableFuture<String> tokenFuture = authS.login(new AuthRequestDTO("test@gmail.com", "4bcdefg!test"));
        String token = (tokenFuture != null)? tokenFuture.join() : null;
        
        System.out.println("Token: " + token);
        System.out.println("Validade do Token: " + jwtTokenProvider.validateToken(token));

        mockMvc.perform(post("/api/course")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(data)))
        
        // Then
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("Course name must be between 4 and 160 characters"));

    }

}
