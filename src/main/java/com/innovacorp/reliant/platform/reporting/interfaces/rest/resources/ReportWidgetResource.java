package com.innovacorp.reliant.platform.reporting.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/** A widget of a section. Enumerations travel in lowercase. */
@Schema(name = "ReportWidget")
public record ReportWidgetResource(
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Integer position,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"process", "session", "recuperation", "fault_cases"}) String variableSource,
    @NotBlank(message = "{validation.not-blank}") @Pattern(regexp = "^[a-z][a-z0-9_]{2,39}$", message = "{validation.pattern}") String variableCode,
    @NotBlank(message = "{validation.not-blank}") String variableLabel,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"value", "min_max", "trend", "table", "text"}) String viewType,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"avg", "min", "max", "range", "last", "count"}) String aggregation,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"half", "full"}) String width
) {
}
