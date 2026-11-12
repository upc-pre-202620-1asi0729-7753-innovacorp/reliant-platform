package com.innovacorp.reliant.platform.reporting.domain.model.commands;

import java.util.List;

/** Replaces the layout of a template. The organization, owner and kind never change. */
public record UpdateReportTemplateCommand(Long reportTemplateId, String name, String description, String primaryColor, String secondaryColor,
                                          String headerText, String footerText, String pageSize, String orientation, boolean showSignatureBlock,
                                          boolean defaultForKind, List<ReportSectionItem> sections) {
}
