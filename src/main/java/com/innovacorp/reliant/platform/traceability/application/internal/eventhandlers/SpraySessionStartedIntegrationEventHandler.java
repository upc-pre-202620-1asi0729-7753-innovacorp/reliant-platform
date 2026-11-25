package com.innovacorp.reliant.platform.traceability.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.processmonitoring.interfaces.events.SpraySessionStartedIntegrationEvent;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.application.commandservices.RecuperationCommandService;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkSessionToRecuperationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link SpraySessionStartedIntegrationEvent}.
 *
 * <p>When Process Monitoring starts a spray session for a recuperation order, this handler links the session to the
 * order (the first link moves it from RECEIVED to IN_PROGRESS). {@code Recuperation.linkSession} is idempotent per
 * session id, so a replayed event never duplicates the link. A failure here is logged and never thrown: the session
 * has already started and that must not be rolled back because traceability could not be updated.</p>
 */
@Service("traceabilitySpraySessionStartedIntegrationEventHandler")
public class SpraySessionStartedIntegrationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpraySessionStartedIntegrationEventHandler.class);
    private final RecuperationCommandService recuperationCommandService;

    public SpraySessionStartedIntegrationEventHandler(RecuperationCommandService recuperationCommandService) {
        this.recuperationCommandService = recuperationCommandService;
    }

    @EventListener
    public void on(SpraySessionStartedIntegrationEvent event) {
        var result = recuperationCommandService.handle(new LinkSessionToRecuperationCommand(event.recuperationId(), event.spraySessionId()));
        if (result instanceof Result.Failure(var error)) {
            LOGGER.warn("Failed to link spray session {} to recuperation {}: {}", event.spraySessionId(), event.recuperationId(), error.message());
        }
    }
}