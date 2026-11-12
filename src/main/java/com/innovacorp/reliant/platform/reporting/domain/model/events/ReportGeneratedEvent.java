package com.innovacorp.reliant.platform.reporting.domain.model.events;

public record ReportGeneratedEvent(Long generatedReportId, Long reportTemplateId, Long organizationId, String subjectType, Long subjectId) {
}
