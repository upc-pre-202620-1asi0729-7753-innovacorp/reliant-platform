package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/** The kind is part of the identity and is not editable. */
@Schema(name = "UpdateReportTemplateRequest")
public record UpdateReportTemplateResource(
    @NotBlank(message = "{validation.not-blank}") String name,
    String description,
    @NotBlank(message = "{validation.not-blank}") @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "{validation.pattern}") String primaryColor,
    @NotBlank(message = "{validation.not-blank}") @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "{validation.pattern}") String secondaryColor,
    String headerText,
    String footerText,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"a4", "letter"}) String pageSize,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"portrait", "landscape"}) String orientation,
    boolean showSignatureBlock,
    boolean defaultForKind,
    @NotEmpty(message = "{validation.not-empty}") List<@Valid ReportSectionResource> sections
) {
}
