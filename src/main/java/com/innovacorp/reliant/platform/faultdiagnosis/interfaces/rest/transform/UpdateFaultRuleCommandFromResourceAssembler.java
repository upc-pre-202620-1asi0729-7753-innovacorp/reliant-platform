package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.UpdateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.UpdateFaultRuleResource;

public class UpdateFaultRuleCommandFromResourceAssembler {
    public static UpdateFaultRuleCommand toCommandFromResource(Long faultRuleId, UpdateFaultRuleResource r) {
        return new UpdateFaultRuleCommand(faultRuleId, r.probableCause(), r.recommendedAction(), r.cooldownSeconds());
    }
}
