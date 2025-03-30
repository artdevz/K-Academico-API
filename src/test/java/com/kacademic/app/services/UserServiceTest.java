package com.kacademic.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.dto.user.UserResponseDTO;
import com.kacademic.app.dto.user.UserUpdateDTO;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.RoleRepository;
import com.kacademic.domain.repositories.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userR;

    @Mock
    private RoleRepository roleR;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userS;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidData_whenCreateUser_thenSuccess() {

        // Given
        UUID roleId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Set <UUID> roles = Set.of(roleId);
        Role mockRole = new Role("STUDENT", "A simple user of K-Academic System");
        mockRole.setId(roleId);
        
        UserRequestDTO data = new UserRequestDTO("ArthurTest", "arthur@gmail.com", "4bcdefg!", roles);

        when(userR.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(roleR.findById(roleId)).thenReturn(Optional.of(mockRole));

        assertEquals("Created User", this.userS.createAsync(data));

        verify(userR, times(1)).save(any());

    }

    @Test
    void givenExistingEmail_whenCreateUser_thenThrowConflict() {

        // Given
        UUID roleId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Set <UUID> roles = Set.of(roleId);
        Role mockRole = new Role("STUDENT", "A simple user of K-Academic System");
        mockRole.setId(roleId);

        when(roleR.findById(roleId)).thenReturn(Optional.of(mockRole));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        String email = "artTest@gmail.com";
        UserRequestDTO data = new UserRequestDTO("ArtTest", email, "4bcdefg!", roles);
        
        // First
        when(userR.findByEmail(anyString())).thenReturn(Optional.empty());

        // Then
        assertEquals("Created User", userS.createAsync(data));
        verify(userR, times(1)).save(any());

        // Second
        when(userR.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        ResponseStatusException thorwn = assertThrows(ResponseStatusException.class, () -> userS.createAsync(data));
        
        // Then
        assertEquals(409, thorwn.getStatusCode().value());
        verify(userR, times(1)).save(any());

    }

    @Test
    void givenInvalidRole_whenCreateUser_thenThrowNotFound() {

        // Given
        UUID invalidRoleId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        Set<UUID> roles = Set.of(invalidRoleId);
        UserRequestDTO data = new UserRequestDTO("ArtTest", "artTest@gmail.com", "4bcdefg!", roles);

        when(userR.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleR.findById(invalidRoleId)).thenReturn(Optional.empty());

        // When
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> userS.createAsync(data));
        
        // Then
        assertEquals(404, thrown.getStatusCode().value());
        verify(userR, never()).save(any());

    }

    @Test
    void givenNoUsers_whenReadAll_thenReturnEmptyList() {

        when(userR.findAll()).thenReturn(List.of());

        // When
        List<UserResponseDTO> result = userS.readAllAsync();

        // Then
        assertTrue(result.isEmpty());

    }

    @Test
    void givenUsersExist_whenReadAll_thenReturnUsersList() {

        // Given
        User user = new User("ArthurTest", "arthur@gmail.com", "hashedPassword", Set.of());
        user.setId(UUID.randomUUID());

        when(userR.findAll()).thenReturn(List.of(user));

        // WHen
        List<UserResponseDTO> result = userS.readAllAsync();

        // Then
        assertEquals(1, result.size());
        assertEquals("ArthurTest", result.get(0).name());

    }

    @Test
    void givenValidId_whenReadById_thenReturnUser() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = new User("ArthurTest", "arthur@gmail.com", "hashedPassword", Set.of());
        user.setId(userId);

        when(userR.findById(userId)).thenReturn(Optional.of(user));

        // When
        UserResponseDTO result = userS.readByIdAsync(userId);

        // Then
        assertEquals(userId, result.id());
        assertEquals("ArthurTest", result.name());

    }

    @Test
    void givenInvalidId_whenReadById_thenThrowNotFound() {

        // Given
        UUID userId = UUID.randomUUID();
        when(userR.findById(userId)).thenReturn(Optional.empty());

        // When
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> userS.readByIdAsync(userId));
        
        // Then
        assertEquals(404, thrown.getStatusCode().value());

    }

    @Test
    void givenValidData_whenUpdateUser_thenSuccess() {
        
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User("ArthurTest", "arthur@gmail.com", "hashedPassword", Set.of());
        user.setId(userId);

        UserUpdateDTO data = new UserUpdateDTO(Optional.of("NewName"), Optional.empty());

        when(userR.findById(userId)).thenReturn(Optional.of(user));

        // When
        String result = userS.updateAsync(userId, data);

        // Then
        assertEquals("Updated User", result);
        assertEquals("NewName", user.getName());

    }

    @Test
    void givenValidDataWithPassword_whenUpdateUser_thenSuccess() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = new User("ArthurTest", "arthur@gmail.com", "oldPassword", Set.of());
        user.setId(userId);

        UserUpdateDTO data = new UserUpdateDTO(Optional.empty(), Optional.of("newPassword"));

        when(userR.findById(userId)).thenReturn(Optional.of(user));

        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> 
            new BCryptPasswordEncoder().encode(invocation.getArgument(0))
        );

        when(userR.save(any(User.class))).thenAnswer(invocation -> {
            User updatedUser = invocation.getArgument(0);
            user.setPassword(updatedUser.getPassword());
            return updatedUser;
        });

        System.out.println("Before update - User Password: " + user.getPassword());

        // When
        String result = userS.updateAsync(userId, data);

        System.out.println("After update - User Password: " + user.getPassword());

        // Then
        assertEquals("Updated User", result);

        assertTrue(passwordEncoder.matches("newPassword", user.getPassword())); // false != true (ERROR)

        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userR, times(1)).save(user);

    }

    @Test
    void givenExistingUser_whenDelete_thenSuccess() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = new User("ArthurTest", "arthur@gmail.com", "hashedPassword", Set.of());
        user.setId(userId);

        when(userR.findById(userId)).thenReturn(Optional.of(user));

        // When
        String result = userS.deleteAsync(userId);

        // Then
        assertEquals("Deleted User", result);
        verify(userR, times(1)).deleteById(userId);

    }

    @Test
    void givenNonExistingUser_whenDelete_thenThrowNotFound() {

        // Given
        UUID userId = UUID.randomUUID();
        when(userR.findById(userId)).thenReturn(Optional.empty());

        // When
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> userS.deleteAsync(userId));
        
        // Then
        assertEquals(404, thrown.getStatusCode().value());
        verify(userR, never()).deleteById(userId);

    }

}
