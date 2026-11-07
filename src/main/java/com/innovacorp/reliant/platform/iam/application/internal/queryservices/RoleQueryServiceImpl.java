package com.innovacorp.reliant.platform.iam.application.internal.queryservices;

import com.innovacorp.reliant.platform.iam.application.queryservices.RoleQueryService;
import com.innovacorp.reliant.platform.iam.domain.model.entities.Role;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.innovacorp.reliant.platform.iam.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleRepository roleRepository;

    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }
}