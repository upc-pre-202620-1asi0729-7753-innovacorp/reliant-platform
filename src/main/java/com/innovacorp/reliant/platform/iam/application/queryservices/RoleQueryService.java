package com.innovacorp.reliant.platform.iam.application.queryservices;

import com.innovacorp.reliant.platform.iam.domain.model.entities.Role;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetAllRolesQuery;

import java.util.List;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
}
