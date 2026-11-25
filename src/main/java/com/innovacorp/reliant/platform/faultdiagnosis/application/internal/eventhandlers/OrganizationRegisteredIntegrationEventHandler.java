package com.innovacorp.reliant.platform.faultdiagnosis.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultRuleCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.SeedDefaultFaultRulesCommand;
import com.innovacorp.reliant.platform.iam.interfaces.events.OrganizationRegisteredIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link OrganizationRegisteredIntegrationEvent}.
 *
 * <p>When a new recuperation supplier signs up in the {@code iam} bounded context, this handler provisions the
 * FESA default cause-effect rules for it, so fault cases can be diagnosed from the very first spray session.
 * Asset owners do not operate HVOF systems and get no rules. The seeding command is idempotent, so a replayed
 * event never duplicates rules. A failure here is logged and never thrown: it must not roll back the sign-up.</p>
 */
@Service("faultDiagnosisOrganizationRegisteredIntegrationEventHandler")
public class OrganizationRegisteredIntegrationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationRegisteredIntegrationEventHandler.class);
    private final FaultRuleCommandService faultRuleCommandService;

    public OrganizationRegisteredIntegrationEventHandler(FaultRuleCommandService faultRuleCommandService) {
        this.faultRuleCommandService = faultRuleCommandService;
    }

    @EventListener
    public void on(OrganizationRegisteredIntegrationEvent event) {
        if (!event.isRecuperationSupplier()) {
            return;
        }
        try {
            faultRuleCommandService.handle(new SeedDefaultFaultRulesCommand(event.organizationId()));
            LOGGER.info("Default fault rules seeded for organization {} ({})", event.organizationId(), event.legalName());
        } catch (Exception e) {
            LOGGER.warn("Failed to seed default fault rules for organization {}: {}", event.organizationId(), e.getMessage());
        }
    }
}