package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ReportTemplateResponse")
public record ReportTemplateResource(Long id, Long organizationId, Long ownerUserId, String name, String description, String reportKind, String primaryColor,
                                     String secondaryColor, String headerText, String footerText, String pageSize, String orientation, boolean showSignatureBlock,
                                     boolean defaultForKind, String status, List<ReportSectionResource> sections) {
}
