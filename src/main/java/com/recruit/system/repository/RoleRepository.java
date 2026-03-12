package com.recruit.system.repository;

import com.recruit.system.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    // Find a role by name
    Optional<Roles> findByName(String name);
}