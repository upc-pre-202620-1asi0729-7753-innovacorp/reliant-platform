package com.innovacorp.reliant.platform.processmonitoring.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.events.SpraySessionStartedEvent;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.events.SpraySessionStartedIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Internal application-layer handler for the {@link SpraySessionStartedEvent} domain event.
 *
 * <p>Translates the internal domain event into a {@link SpraySessionStartedIntegrationEvent} and re-publishes it on
 * the Spring event bus, the only place a domain event crosses from {@code processmonitoring.domain} into
 * {@code processmonitoring.interfaces}.</p>
 */
@Service("processMonitoringSpraySessionStartedEventHandler")
public class SpraySessionStartedEventHandler {
    private final ApplicationEventPublisher eventPublisher;

    public SpraySessionStartedEventHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void on(SpraySessionStartedEvent event) {
        eventPublisher.publishEvent(new SpraySessionStartedIntegrationEvent(
                event.spraySessionId(), event.organizationId(), event.hvofSystemId(), event.recuperationId(), event.recipeNumber()));
    }
}