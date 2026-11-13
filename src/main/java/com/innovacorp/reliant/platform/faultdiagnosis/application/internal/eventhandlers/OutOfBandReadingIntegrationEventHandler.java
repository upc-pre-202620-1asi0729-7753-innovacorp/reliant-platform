package com.innovacorp.reliant.platform.faultdiagnosis.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultCaseCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.OpenFaultCaseCommand;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.events.OutOfBandReadingIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service("faultDiagnosisOutOfBandReadingIntegrationEventHandler")
public class OutOfBandReadingIntegrationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutOfBandReadingIntegrationEventHandler.class);
    private final FaultCaseCommandService faultCaseCommandService;

    public OutOfBandReadingIntegrationEventHandler(FaultCaseCommandService faultCaseCommandService) {
        this.faultCaseCommandService = faultCaseCommandService;
    }

    @EventListener
    public void on(OutOfBandReadingIntegrationEvent event) {
        try {
            faultCaseCommandService.handle(new OpenFaultCaseCommand(event.organizationId(), event.spraySessionId(), event.hvofSystemId(),
                            event.parameter(), event.band(), event.value(), event.epochMillis()))
                    .ifPresent(c -> LOGGER.info("Fault case {} opened: session {} {} {} = {} {}", c.getId(), event.spraySessionId(), event.parameter(), event.band(), event.value(), event.unitSymbol()));
        } catch (Exception e) {
            // Never break the readings ingestion because diagnosis failed.
            LOGGER.error("Fault diagnosis failed for session {} {}: {}", event.spraySessionId(), event.parameter(), e.getMessage());
        }
    }
}
