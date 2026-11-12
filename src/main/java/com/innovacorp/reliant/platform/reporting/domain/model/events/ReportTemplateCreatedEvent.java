package com.innovacorp.reliant.platform.reporting.domain.model.events;

public record ReportTemplateCreatedEvent(Long reportTemplateId, Long organizationId, String reportKind) {
}
