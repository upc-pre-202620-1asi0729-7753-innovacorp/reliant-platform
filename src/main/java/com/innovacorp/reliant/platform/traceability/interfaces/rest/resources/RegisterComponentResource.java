package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "RegisterComponentRequest", example = "{\"customerId\": 1, \"serialNumber\": \"HR-7781\", \"partNumber\": \"707-00-0X340\", \"componentType\": \"HYDRAULIC_ROD\", \"machineManufacturer\": \"Komatsu\", \"machineModel\": \"PC5500\", \"pcrTargetHours\": 6000}")
public record RegisterComponentResource(
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") @Schema(example = "1") Long customerId,
    @NotBlank(message = "{validation.not-blank}") @Schema(example = "HR-7781") String serialNumber,
    @NotBlank(message = "{validation.not-blank}") @Schema(example = "707-00-0X340") String partNumber,
    @NotBlank(message = "{validation.not-blank}") @Schema(example = "HYDRAULIC_ROD", allowableValues = {"HYDRAULIC_ROD", "CYLINDER_BLOCK", "SHAFT", "IMPELLER", "OTHER"}) String componentType,
    @NotBlank(message = "{validation.not-blank}") @Schema(example = "Komatsu") String machineManufacturer,
    @NotBlank(message = "{validation.not-blank}") @Schema(example = "PC5500") String machineModel,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") @Schema(example = "6000") Integer pcrTargetHours
) {
}
