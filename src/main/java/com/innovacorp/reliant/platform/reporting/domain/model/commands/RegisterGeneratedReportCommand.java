package com.innovacorp.reliant.platform.reporting.domain.model.commands;

import java.time.LocalDate;

/** Records that a report was rendered from a template for a subject (the file itself stays with the user). */
public record RegisterGeneratedReportCommand(Long reportTemplateId, Long organizationId, Long generatedByUserId, String subjectType, Long subjectId,
                                             String fileName, LocalDate periodFrom, LocalDate periodTo) {
}
