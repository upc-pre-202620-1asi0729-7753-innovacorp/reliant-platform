package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands;

public record UpdateFaultRuleCommand(Long faultRuleId, String probableCause, String recommendedAction, Integer cooldownSeconds) {
}
