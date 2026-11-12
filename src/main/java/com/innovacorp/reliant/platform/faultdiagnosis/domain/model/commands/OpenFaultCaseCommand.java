package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands;

/** Built by the integration event handler, never by a controller. */
public record OpenFaultCaseCommand(Long organizationId, Long spraySessionId, Long hvofSystemId, String parameter, String band, double value, long epochMillis) {
}
