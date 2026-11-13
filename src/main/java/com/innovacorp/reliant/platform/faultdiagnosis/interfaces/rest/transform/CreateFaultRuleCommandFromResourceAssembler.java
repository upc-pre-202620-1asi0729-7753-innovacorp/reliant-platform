package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.CreateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.CreateFaultRuleResource;

public class CreateFaultRuleCommandFromResourceAssembler {
    /** organizationId is null on purpose: the command service fills it from the authenticated user. */
    public static CreateFaultRuleCommand toCommandFromResource(CreateFaultRuleResource r) {
        return new CreateFaultRuleCommand(null, r.parameter(), r.band(), r.probableCause(), r.recommendedAction(), r.cooldownSeconds());
    }
}
