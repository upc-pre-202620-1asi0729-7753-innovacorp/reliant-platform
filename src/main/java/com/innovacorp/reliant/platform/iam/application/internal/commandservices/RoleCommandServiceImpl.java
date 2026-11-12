package com.innovacorp.reliant.platform.iam.application.internal.commandservices;

import com.innovacorp.reliant.platform.iam.application.commandservices.RoleCommandService;
import com.innovacorp.reliant.platform.iam.domain.model.commands.SeedRolesCommand;
import com.innovacorp.reliant.platform.iam.domain.model.entities.Role;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Roles;
import com.innovacorp.reliant.platform.iam.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Seeds every role of the Roles enum that does not exist yet, in declaration order.
 */
@Service
public class RoleCommandServiceImpl implements RoleCommandService {

    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}
