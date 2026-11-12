package com.innovacorp.reliant.platform.processmonitoring.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.events.OutOfBandReadingDetectedEvent;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.events.OutOfBandReadingIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service("processMonitoringOutOfBandReadingDetectedEventHandler")
public class OutOfBandReadingDetectedEventHandler {

    private final ApplicationEventPublisher applicationEventPublisher;

    public OutOfBandReadingDetectedEventHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener
    public void on(OutOfBandReadingDetectedEvent event) {
        applicationEventPublisher.publishEvent(new OutOfBandReadingIntegrationEvent(
                event.spraySessionId(), event.hvofSystemId(), event.organizationId(),
                event.parameter(), event.band(), event.value(), event.epochMillis(),
                event.unitSymbol()));
    }
}