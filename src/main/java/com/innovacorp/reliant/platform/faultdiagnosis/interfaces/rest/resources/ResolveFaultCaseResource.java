package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "ResolveFaultCaseRequest", example = "{\"resolutionNotes\": \"Regulator replaced, pressure back to 7.2 bar\"}")
public record ResolveFaultCaseResource(@Size(max = 500, message = "{validation.size}") String resolutionNotes) {
}
