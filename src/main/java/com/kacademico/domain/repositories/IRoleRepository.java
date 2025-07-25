package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Role;

public interface IRoleRepository {
    
    List<Role> findAll();
    Optional<Role> findById(UUID id);
    Role save(Role role);
    void deleteById(UUID id);

    Optional<Role> findByName(String name);

}
