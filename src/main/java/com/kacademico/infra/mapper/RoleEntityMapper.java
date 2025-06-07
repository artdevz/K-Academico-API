package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Role;
import com.kacademico.infra.entities.RoleEntity;

public class RoleEntityMapper {
    
    public static Role toDomain(RoleEntity entity) {
        if (entity == null) return null;
        Role role = new Role(
            entity.getId(),
            entity.getName(),
            entity.getDescription()
        );
        return role;
    }

    public static RoleEntity toEntity(Role role) {
        if (role == null) return null;
        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        entity.setDescription(role.getDescription());

        return entity;
    }

}
