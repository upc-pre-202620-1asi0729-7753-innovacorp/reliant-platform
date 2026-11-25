
package com.innovacorp.reliant.platform.iam.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.iam.domain.model.events.OrganizationRegisteredEvent;
import com.innovacorp.reliant.platform.iam.interfaces.events.OrganizationRegisteredIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Internal application-layer handler for the {@link OrganizationRegisteredEvent} domain event.
 *
 * <p>Translates the internal domain event into an {@link OrganizationRegisteredIntegrationEvent} and
 * re-publishes it on the Spring event bus: the only place a domain event crosses from
 * {@code iam.domain} into {@code iam.interfaces}.</p>
 */
@Service("iamOrganizationRegisteredEventHandler")
public class OrganizationRegisteredEventHandler {
    private final ApplicationEventPublisher eventPublisher;

    public OrganizationRegisteredEventHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void on(OrganizationRegisteredEvent event) {
        eventPublisher.publishEvent(new OrganizationRegisteredIntegrationEvent(
                event.organizationId(), event.legalName(), event.ruc(), event.organizationType()));
    }
}
