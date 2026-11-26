package com.innovacorp.reliant.platform.iam.application.acl;

import com.innovacorp.reliant.platform.iam.application.queryservices.OrganizationQueryService;
import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetOrganizationByIdQuery;
import com.innovacorp.reliant.platform.iam.domain.model.queries.GetOrganizationByRucQuery;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

/**
 * Application-layer implementation of the IAM ACL facade. Delegates to iam's own query services and converts their
 * {@code Optional} into the sentinel primitives the facade promises.
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final OrganizationQueryService organizationQueryService;

    public IamContextFacadeImpl(OrganizationQueryService organizationQueryService) {
        this.organizationQueryService = organizationQueryService;
    }

    @Override
    public Long fetchAssetOwnerOrganizationIdByRuc(String ruc) {
        try {
            return organizationQueryService.handle(new GetOrganizationByRucQuery(new Ruc(ruc)))
                    .filter(organization -> !organization.isSupplier())
                    .map(Organization::getId)
                    .orElse(0L);
        } catch (IllegalArgumentException invalidRuc) {
            return 0L;
        }
    }

    @Override
    public String fetchOrganizationLegalNameById(Long organizationId) {
        return organizationQueryService.handle(new GetOrganizationByIdQuery(organizationId))
                .map(Organization::getLegalName)
                .orElse("");
    }
}
