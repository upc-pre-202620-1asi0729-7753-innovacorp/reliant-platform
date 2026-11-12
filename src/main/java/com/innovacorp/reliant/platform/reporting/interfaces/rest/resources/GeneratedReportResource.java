package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Date;

@Schema(name = "GeneratedReportResponse")
public record GeneratedReportResource(Long id, Long reportTemplateId, Long organizationId, Long generatedByUserId, String subjectType, Long subjectId,
                                      String format, String fileName, LocalDate periodFrom, LocalDate periodTo, Date generatedAt) {
}
