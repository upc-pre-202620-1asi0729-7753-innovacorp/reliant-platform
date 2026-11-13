package com.innovacorp.reliant.platform.reporting.domain.model.commands;

public record ChangeReportTemplateStatusCommand(Long reportTemplateId, String status) {
}
