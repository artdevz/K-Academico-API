package com.kacademico.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.models.User;

@Repository
public interface LoginRepository extends JpaRepository<User, UUID> {
    
    public Optional<User> findByEmail(String login);

}
