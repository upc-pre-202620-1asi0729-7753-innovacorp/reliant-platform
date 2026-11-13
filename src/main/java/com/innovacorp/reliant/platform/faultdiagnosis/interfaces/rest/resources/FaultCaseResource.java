package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "FaultCaseResponse")
public record FaultCaseResource(Long id, Long organizationId, Long spraySessionId, Long hvofSystemId, String parameter, String band, double value, long epochMillis,
                                String probableCause, String recommendedAction, String status, Long acknowledgedBy, Date acknowledgedAt,
                                Long resolvedBy, Date resolvedAt, String resolutionNotes) {
}
