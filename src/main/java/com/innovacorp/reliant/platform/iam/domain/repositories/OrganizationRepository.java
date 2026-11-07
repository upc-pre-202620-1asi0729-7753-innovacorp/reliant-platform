package com.innovacorp.reliant.platform.iam.domain.repositories;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;

import java.util.Optional;

/**
 * Organization repository port.
 */
public interface OrganizationRepository {

    Organization save(Organization organization);

    boolean existsByRuc(Ruc ruc);

    Optional<Organization> findById(Long id);
}