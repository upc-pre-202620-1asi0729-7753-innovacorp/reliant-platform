package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.entities.OrganizationPersistenceEntity;

public final class OrganizationPersistenceAssembler {

    private OrganizationPersistenceAssembler() {
    }

    public static Organization toDomainFromPersistence(OrganizationPersistenceEntity entity) {
        if (entity == null) return null;
        return Organization.reconstitute(entity.getId(), entity.getLegalName(), entity.getRuc(), entity.getOrganizationType(), entity.getStatus());
    }

    public static OrganizationPersistenceEntity toPersistenceFromDomain(Organization organization) {
        if (organization == null) return null;
        var entity = new OrganizationPersistenceEntity();
        if (organization.getId() != null) {
            entity.setId(organization.getId());
        }
        entity.setLegalName(organization.getLegalName());
        entity.setRuc(organization.getRuc());
        entity.setOrganizationType(organization.getOrganizationType());
        entity.setStatus(organization.getStatus());
        return entity;
    }
}