package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(name = "RegisterGeneratedReportRequest", example = "{\"reportTemplateId\": 2, \"subjectType\": \"spray_session\", \"subjectId\": 1, \"fileName\": \"session_report-session-1.pdf\", \"periodFrom\": \"2026-11-11\", \"periodTo\": \"2026-11-11\"}")
public record RegisterGeneratedReportResource(
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long reportTemplateId,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"spray_session", "recuperation"}) String subjectType,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long subjectId,
    @NotBlank(message = "{validation.not-blank}") String fileName,
    LocalDate periodFrom,
    LocalDate periodTo
) {
}
