package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.events;

public record FaultCaseOpenedEvent(Long faultCaseId, Long organizationId, Long spraySessionId, String parameter, String band, String probableCause) {
}
