package com.recruit.system.config;

import com.recruit.system.model.Roles;
import com.recruit.system.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        // Only add roles if table is empty
        if (roleRepository.count() == 0) {
            roleRepository.save(new Roles(null, "APPLICANT"));
            roleRepository.save(new Roles(null, "HR"));
            roleRepository.save(new Roles(null, "SUPERADMIN"));
            System.out.println("✅ Default roles created: APPLICANT, HR, SUPERADMIN");
        }
    }
}