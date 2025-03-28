package com.kacademic.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademic.models.AppRole;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, UUID> {

    Optional<AppRole> findByName(String name);

}
