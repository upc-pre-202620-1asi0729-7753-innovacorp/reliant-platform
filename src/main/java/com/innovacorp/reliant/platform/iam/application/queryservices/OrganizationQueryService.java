package com.innovacorp.reliant.platform.iam.application.queryservices;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetOrganizationByIdQuery;

import java.util.Optional;

public interface OrganizationQueryService {
    Optional<Organization> handle(GetOrganizationByIdQuery query);
}