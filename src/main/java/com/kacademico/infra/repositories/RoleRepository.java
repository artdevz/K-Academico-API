package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Role;
import com.kacademico.domain.repositories.IRoleRepository;
import com.kacademico.infra.repositories.jpa.RoleJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class RoleRepository implements IRoleRepository {
    
    private final RoleJpaRepository jpa;

    @Override
    public List<Role> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Role> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Role save(Role role) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }

}
