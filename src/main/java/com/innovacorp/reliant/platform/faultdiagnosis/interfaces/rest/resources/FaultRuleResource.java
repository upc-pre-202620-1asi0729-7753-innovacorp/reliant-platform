package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FaultRuleResponse")
public record FaultRuleResource(Long id, Long organizationId, String parameter, String band, String probableCause, String recommendedAction, int cooldownSeconds) {
}
