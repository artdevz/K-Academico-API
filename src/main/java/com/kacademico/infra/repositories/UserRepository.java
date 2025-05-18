package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.User;
import com.kacademico.domain.repositories.IUserRepository;
import com.kacademico.infra.entities.UserEntity;
import com.kacademico.infra.mapper.UserEntityMapper;
import com.kacademico.infra.repositories.jpa.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepository implements IUserRepository {
    
    private final UserJpaRepository jpa;

    @Override
    public List<User> findAll() {
        return jpa.findAll().stream().map(UserEntityMapper::toBaseDomain).toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id).map(UserEntityMapper::toDomain);
    }
    
    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        UserEntity saved = jpa.save(entity);
        
        return UserEntityMapper.toDomain(saved);
    }
    
    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(UserEntityMapper::toDomain);
    }

}
