package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands;

public record ResolveFaultCaseCommand(Long faultCaseId, String resolutionNotes) {
}
