package com.innovacorp.reliant.platform.reporting.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.reporting.domain.model.events.ReportGeneratedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ReportGeneratedEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGeneratedEventHandler.class);

    @EventListener
    public void on(ReportGeneratedEvent event) {
        LOGGER.info("Report {} generated from template {} for {} {} (organization {})", event.generatedReportId(), event.reportTemplateId(),
                event.subjectType(), event.subjectId(), event.organizationId());
    }
}
