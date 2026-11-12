package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(name = "ReportSection")
public record ReportSectionResource(
    @NotBlank(message = "{validation.not-blank}") String title,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Integer sectionOrder,
    @NotEmpty(message = "{validation.not-empty}") List<@Valid ReportWidgetResource> widgets
) {
}
