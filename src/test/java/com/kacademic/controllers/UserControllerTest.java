package com.kacademic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.models.User;
import com.kacademic.security.JwtTokenProvider;
import com.kacademic.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userS;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {

        String fakeJwtToken = generateFakeJwtToken();

        when(jwtTokenProvider.resolveToken(any())).thenReturn(fakeJwtToken);
        when(jwtTokenProvider.validateToken(fakeJwtToken)).thenReturn(true);
        
        UserDetails userDetails = new User("testuser", "arthurTest123@gmail.com", "4bcdefg!");
        when(jwtTokenProvider.getAuthentication(fakeJwtToken)).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

    }

    private String generateFakeJwtToken() {
        
        String username = "testuser";
        long expirationTimeMs = 3600000L;
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);
        Key key = new SecretKeySpec("thisIsASecretKeyForJwtHMAC256bitsLong".getBytes(), SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", UUID.randomUUID().toString());

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact();

    }

    @Test
    @DisplayName("Given a name shorter than 3 characters, when creating a user, then should throw validation error")
    void givenShortName_whenCreateUser_thenThrowValidationError() throws Exception {

        // Arrange
        UserRequestDTO data = new UserRequestDTO("Ar", "arthurLess@gmail.com", "4bcdefg!");

        when(userS.createAsync(data)).thenThrow(new IllegalArgumentException("Name must be between 3 and 48 characters"));

        // Act & Assert
        mockMvc.perform(post("/api/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateFakeJwtToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(data)))
            .andExpect(status().isUnprocessableEntity())  // 422 Unprocessable Entity
            .andExpect(jsonPath("$.message").value("Name must be between 3 and 48 characters"));
    }

    @Test
    @DisplayName("Given a name longer than 48 characters, when creating a user, then should throw validation error")
    void givenLongName_whenCreateUser_thenThrowValidationError() throws Exception {

        // Arrange
        UserRequestDTO data = new UserRequestDTO("ArthurArthurArthurArthurArthurArthurArthurArthurA", "arthurMost@gmail.com", "4bcdefg!");

        BindingResult bindingResult = new BeanPropertyBindingResult(data, "userRequestDTO");
        bindingResult.addError(new FieldError("userRequestDTO", "name", "Name must be between 3 and 48 characters"));
        
        IllegalArgumentException exception = new IllegalArgumentException("Name must be between 3 and 48 characters");

        when(userS.createAsync(data)).thenThrow((exception));

        System.out.println("Func: " + generateFakeJwtToken());

        // Act & Assert
        mockMvc.perform(post("/api/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + generateFakeJwtToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(data)))
            .andExpect(status().isUnprocessableEntity())  // 422 Unprocessable Entity
            .andExpect(jsonPath("$.message").value("Name must be between 3 and 48 characters"));
    }

}
