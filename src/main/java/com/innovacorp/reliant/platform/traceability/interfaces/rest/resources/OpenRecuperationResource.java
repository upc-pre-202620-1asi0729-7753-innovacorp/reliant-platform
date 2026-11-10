package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Schema(name = "OpenRecuperationRequest")
public record OpenRecuperationResource(
        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "^[A-Z0-9-]{4,30}$", message = "{validation.pattern}")
        String workOrderNumber,

        @NotBlank(message = "{validation.not-blank}")
        String manufacturingOrderNumber,

        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        Long componentId,

        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        Long customerId,

        String segment,

        @NotBlank(message = "{validation.not-blank}")
        String operation,

        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        Double weightValue,

        String weightUnitSymbol,

        @NotNull(message = "{validation.not-blank}")
        @PositiveOrZero
        Integer hourmeterAtEntry,

        @NotBlank(message = "{validation.not-blank}")
        String powderSupplier,

        @NotBlank(message = "{validation.not-blank}")
        String powderLotNumber,

        @NotBlank(message = "{validation.not-blank}")
        String powderChemicalComposition,

        LocalDate receivedAt
) {
}