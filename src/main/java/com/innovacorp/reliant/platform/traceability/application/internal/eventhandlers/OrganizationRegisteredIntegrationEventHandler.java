package com.innovacorp.reliant.platform.traceability.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.iam.interfaces.events.OrganizationRegisteredIntegrationEvent;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.application.commandservices.CustomerCommandService;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkAssetOwnerToCustomersCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link OrganizationRegisteredIntegrationEvent}.
 *
 * <p>When a mining company signs up as an ASSET_OWNER, every customer a supplier had already registered with the same
 * RUC gets linked to that organization, so the asset owner sees its components and recuperations from its first
 * sign-in. Linking is idempotent, and a failure is logged and never thrown: it must not roll back the sign-up.</p>
 */
@Service("traceabilityOrganizationRegisteredIntegrationEventHandler")
public class OrganizationRegisteredIntegrationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationRegisteredIntegrationEventHandler.class);
    private final CustomerCommandService customerCommandService;

    public OrganizationRegisteredIntegrationEventHandler(CustomerCommandService customerCommandService) {
        this.customerCommandService = customerCommandService;
    }

    @EventListener
    public void on(OrganizationRegisteredIntegrationEvent event) {
        if (!event.isAssetOwner()) {
            return;
        }
        var result = customerCommandService.handle(new LinkAssetOwnerToCustomersCommand(event.ruc(), event.organizationId()));
        switch (result) {
            case Result.Success(var linked) -> LOGGER.info("{} customer(s) with RUC {} linked to asset owner organization {}", linked.size(), event.ruc(), event.organizationId());
            case Result.Failure(var error) -> LOGGER.warn("Failed to link customers with RUC {} to organization {}: {}", event.ruc(), event.organizationId(), error.message());
        }
    }
}
