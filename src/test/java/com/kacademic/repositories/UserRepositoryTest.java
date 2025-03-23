package com.kacademic.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.models.User;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userR;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User Details successfully from DB")
    void testFindByEmailCase1() {

        // Arrange
        String email = "arthurTest@gmail.com";
        UserRequestDTO data = new UserRequestDTO("ArthurTest", email, "4bcdefg!");
        this.createUser(data);

        // Act
        Optional<UserDetails> result = this.userR.findByEmail(email);

        // Assert
        assertThat(result.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Should not get User Details from DB when user not exists")
    void testFindByEmailCase2() {

        // Arrange
        String email = "arthurTest@gmail.com";

        // Act
        Optional<UserDetails> result = this.userR.findByEmail(email);

        // Assert
        assertThat(result.isEmpty()).isTrue();

    }

    private User createUser(UserRequestDTO data) {
        User newUser = new User(data.name(), data.email(), data.password());
        this.entityManager.persist(newUser);
        return newUser;
    }
    
}
