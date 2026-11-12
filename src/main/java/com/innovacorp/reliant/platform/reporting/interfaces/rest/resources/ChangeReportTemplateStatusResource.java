package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ChangeReportTemplateStatusRequest", example = "{\"status\": \"archived\"}")
public record ChangeReportTemplateStatusResource(
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"active", "archived"}) String status
) {
}
