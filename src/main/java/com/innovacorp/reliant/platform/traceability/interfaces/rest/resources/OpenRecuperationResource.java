package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(name = "OpenRecuperationRequest", example = "{\"workOrderNumber\": \"OT-2026-0451\", \"manufacturingOrderNumber\": \"OF-88210\", \"componentId\": 1, \"customerId\": 1, \"segment\": \"Mining\", \"operation\": \"HVOF WC-Co on rod\", \"weightValue\": 185.5, \"weightUnitSymbol\": \"kg\", \"hourmeterAtEntry\": 11840, \"powderSupplier\": \"Oerlikon Metco\", \"powderLotNumber\": \"WOKA-3102-L2206\", \"powderChemicalComposition\": \"WC-12Co\", \"receivedAt\": \"2026-10-20\"}")
public record OpenRecuperationResource(
    @NotBlank(message = "{validation.not-blank}") @Pattern(regexp = "^[A-Z0-9-]{4,30}$", message = "{validation.pattern}") String workOrderNumber,
    @NotBlank(message = "{validation.not-blank}") String manufacturingOrderNumber,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long componentId,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long customerId,
    String segment,
    @NotBlank(message = "{validation.not-blank}") String operation,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Double weightValue,
    String weightUnitSymbol,
    @NotNull(message = "{validation.not-blank}") @PositiveOrZero(message = "{validation.positive}") Integer hourmeterAtEntry,
    @NotBlank(message = "{validation.not-blank}") String powderSupplier,
    @NotBlank(message = "{validation.not-blank}") String powderLotNumber,
    @NotBlank(message = "{validation.not-blank}") String powderChemicalComposition,
    LocalDate receivedAt
) {
}
