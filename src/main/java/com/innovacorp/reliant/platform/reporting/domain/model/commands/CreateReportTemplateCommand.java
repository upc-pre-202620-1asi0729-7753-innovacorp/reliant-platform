package com.innovacorp.reliant.platform.reporting.domain.model.commands;

import java.util.List;

public record CreateReportTemplateCommand(Long organizationId, Long ownerUserId, String name, String description, String reportKind, String primaryColor,
                                          String secondaryColor, String headerText, String footerText, String pageSize, String orientation,
                                          boolean showSignatureBlock, boolean defaultForKind, List<ReportSectionItem> sections) {
}
