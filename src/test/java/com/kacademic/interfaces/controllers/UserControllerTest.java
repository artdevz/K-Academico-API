package com.kacademic.interfaces.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.services.UserService;
import com.kacademic.infra.security.JwtTokenProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import com.kacademic.infra.configs.SecurityTestConfig;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Import(SecurityTestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userS;

    @MockBean
    private JwtTokenProvider jwtTokenP;

    @Test
    void givenValidData_whenUserCreated_thenReturn201() throws Exception {

        // Given
        String jwt = generateJWT();

        UserRequestDTO request = new UserRequestDTO("User Tester", "test@gmail.com", "4bcdefG!", Set.of(UUID.randomUUID()));
        when(userS.createAsync(any())).thenReturn("User Created");

        when(jwtTokenP.validateToken(any())).thenReturn(true);
        when(jwtTokenP.resolveToken(any(HttpServletRequest.class))).thenReturn(jwt);
        when(jwtTokenP.getAuthentication(any())).thenReturn(new UsernamePasswordAuthenticationToken("test@gmail.com", null, AuthorityUtils.createAuthorityList("ADMIN")));

        // When
        ResultActions response = mockMvc.perform(post("/user")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            );

        // Then
        response.andExpect(status().isCreated())
            .andExpect(content().string("User Created"));

    }

    @Test
    void givenShortName_whenUserCreated_thenReturn422() throws Exception {

        // Given
        String jwt = generateJWT();

        UserRequestDTO request = new UserRequestDTO("Ts", "test@gmail.com", "4bcdefG!", Set.of(UUID.randomUUID()));
        when(userS.createAsync(any())).thenReturn("User Created");

        when(jwtTokenP.validateToken(any())).thenReturn(true);
        when(jwtTokenP.resolveToken(any(HttpServletRequest.class))).thenReturn(jwt);
        when(jwtTokenP.getAuthentication(any())).thenReturn(new UsernamePasswordAuthenticationToken("test@gmail.com", null, AuthorityUtils.createAuthorityList("ADMIN")));

        // When
        ResultActions response = mockMvc.perform(post("/user")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            );

        // Then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("Name must be between 3 and 48 characters"));

    }
    
    @Test
    void givenLongName_whenUserCreated_thenReturn422() throws Exception {

        // Given
        String jwt = generateJWT();

        UserRequestDTO request = new UserRequestDTO("TestingTestingTestingTestingTestingTestingTesting", "test@gmail.com", "4bcdefG!", Set.of(UUID.randomUUID()));
        when(userS.createAsync(any())).thenReturn("User Created");

        when(jwtTokenP.validateToken(any())).thenReturn(true);
        when(jwtTokenP.resolveToken(any(HttpServletRequest.class))).thenReturn(jwt);
        when(jwtTokenP.getAuthentication(any())).thenReturn(new UsernamePasswordAuthenticationToken("test@gmail.com", null, AuthorityUtils.createAuthorityList("ADMIN")));

        // When
        ResultActions response = mockMvc.perform(post("/user")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            );

        // Then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("Name must be between 3 and 48 characters"));

    }

    @Test
    void givenInvalidEmail_whenCreateUser_thenReturn422() throws Exception {

        // Given
        String jwt = generateJWT();

        UserRequestDTO request = new UserRequestDTO("User Tester", "userTesterGmail.com", "4bcdefG!", Set.of(UUID.randomUUID()));
        when(userS.createAsync(any())).thenReturn("User Created");

        when(jwtTokenP.validateToken(any())).thenReturn(true);
        when(jwtTokenP.resolveToken(any(HttpServletRequest.class))).thenReturn(jwt);
        when(jwtTokenP.getAuthentication(any())).thenReturn(new UsernamePasswordAuthenticationToken("test@gmail.com", null, AuthorityUtils.createAuthorityList("ADMIN")));

        // When
        ResultActions response = mockMvc.perform(post("/user")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            );

        // Then
        response.andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value("must be a well-formed email address"));

    }
    
    @Test
    void givenInvalidPasswordFormat_whenCreateUser_thenReturn422() throws Exception {

        // Given
        String jwt = generateJWT();

        ArrayList<String> passwords = new ArrayList<>(List.of(
            "Passwo!", // Less than 8
            "123456789012345678901234567890123", // Most than 32
            "12345678", // Only Number
            "password", // Only Lowercase Letters
            "PASSWORD", // Only Uppercase Letters
            "Password", // Only Letters
            "!@#$%¨&*", // Only Symbols
            "abcd1234", // Only Lowercase Letters and Numbers
            "ABCD1234", // Only Uppercase Letters and Numbers
            "ABcd1234", // Only Letters and Numbers
            "abcdefg!", // Only Lowercase Letters and Symbols
            "ABCDEFG!", // Only Uppercase Letters and Symbols
            "ABCDefg!", // Only Letters and Symbols
            "1234567!", // Only Numbers and Symbols
            "4bcdefg!", // Missing Uppercase Letters
            "4BCDEFG!" // Missing Lowercase Letters
        ));

        when(jwtTokenP.validateToken(any())).thenReturn(true);
        when(jwtTokenP.resolveToken(any(HttpServletRequest.class))).thenReturn(jwt);
        when(jwtTokenP.getAuthentication(any())).thenReturn(new UsernamePasswordAuthenticationToken("test@gmail.com", null, AuthorityUtils.createAuthorityList("ADMIN")));

        for (int i = 0; i < passwords.size(); i++) {

            UserRequestDTO request = new UserRequestDTO("User Tester" + i, "test" + i + "@gmail.com", passwords.get(i), Set.of(UUID.randomUUID()));
            when(userS.createAsync(any())).thenReturn("User Created");

            // When
            ResultActions response = mockMvc.perform(post("/user")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                );

            // Then
            response.andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value(
                    "The password must contain at least one lowercase letter, one uppercase letter, one number, one special character, and be between 8 and 32 characters"));

        }

    }

    private String generateJWT() {
        String SECRET_KEY = "MySuperSecretKeyWith32BytesHere!";
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", UUID.randomUUID().toString());
        extraClaims.put("name", "User Tester");
        extraClaims.put("roles", Set.of("ADMIN"));

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject("test@gmail.com")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .setHeaderParam("alg", "HS256")
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    }

}
