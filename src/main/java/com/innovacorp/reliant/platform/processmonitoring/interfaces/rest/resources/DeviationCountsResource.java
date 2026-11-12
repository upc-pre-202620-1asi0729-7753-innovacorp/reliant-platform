package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DeviationCountsResponse", example = "{\"warning\": 3, \"shutdown\": 1}")
public record DeviationCountsResource(long warning, long shutdown) {
}
