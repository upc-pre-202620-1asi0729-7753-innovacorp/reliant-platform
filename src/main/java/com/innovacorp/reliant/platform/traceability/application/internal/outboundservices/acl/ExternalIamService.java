package com.innovacorp.reliant.platform.traceability.application.internal.outboundservices.acl;

import com.innovacorp.reliant.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ACL service used by Traceability to interact with IAM capabilities (the consumer side of the anti-corruption
 * layer). Maps the facade's {@code 0L} sentinel to {@code Optional.empty()}: Traceability never sees the raw sentinel
 * past this class.
 */
@Service
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /** @return the id of the asset-owner organization registered with the customer's RUC, when it already signed up. */
    public Optional<Long> fetchAssetOwnerOrganizationIdByRuc(String ruc) {
        var organizationId = iamContextFacade.fetchAssetOwnerOrganizationIdByRuc(ruc);
        return organizationId == 0L ? Optional.empty() : Optional.of(organizationId);
    }
}
