package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands;

public record CreateFaultRuleCommand(Long organizationId, String parameter, String band, String probableCause, String recommendedAction, Integer cooldownSeconds) {
}
