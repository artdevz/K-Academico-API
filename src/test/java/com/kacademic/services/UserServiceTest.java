package com.kacademic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.models.User;
import com.kacademic.repositories.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userR;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userS;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create User successfully when everything is OK")
    void testCreateAsyncCase0() throws ExecutionException, InterruptedException {

        // Arrange
        UserRequestDTO data = new UserRequestDTO("ArthurTest2", "arthurTest2@gmail.com", "4bcdefg!");

        when(userR.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userR.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CompletableFuture<String> result = this.userS.createAsync(data);
        String response = result.get();

        // Assert
        assertEquals("Created User", response);

        verify(userR, times(1)).save(any());

    }

    @Test
    @DisplayName("Should create multiple users and handle email conflict")
    void testCreateAsyncCase1() throws ExecutionException, InterruptedException {

        // Arrange
        for (int i = 0; i < 100; i++) {
            String email = "arthurTest" + i + "@gmail.com";
            UserRequestDTO data = new UserRequestDTO("ArthurTest" + i, email, "4bcdefg!");
        
            if (i > 0) when(userR.findByEmail(anyString())).thenReturn(Optional.of(new User()));
            else when(userR.findByEmail(anyString())).thenReturn(null);

            when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
            when(userR.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            
            // Act and Assert
            try {
                CompletableFuture<String> result = this.userS.createAsync(data);
                String response = result.get();

                if (i == 0) assertEquals("Created User", response);
                else fail("Expected a conflict for user " + i + " but no exception was thrown.");

            }
            
            catch (ResponseStatusException e) {
                assertEquals("Email already being used", e.getReason());
            }

            verify(userR, times(1)).save(any());

        }

    }

    /*
    @Test
    void testDeleteAsync() {

    }

    @Test
    void testReadAllAsync() {

    }

    @Test
    void testReadByIdAsync() {

    }

    @Test
    void testUpdateAsync() {

    } */

}
