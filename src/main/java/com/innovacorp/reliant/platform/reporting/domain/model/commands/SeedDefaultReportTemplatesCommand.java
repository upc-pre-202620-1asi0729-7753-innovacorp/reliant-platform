package com.innovacorp.reliant.platform.reporting.domain.model.commands;

/** Creates the standard templates of an organization the first time it opens Reporting. */
public record SeedDefaultReportTemplatesCommand(Long organizationId, Long ownerUserId) {
}
