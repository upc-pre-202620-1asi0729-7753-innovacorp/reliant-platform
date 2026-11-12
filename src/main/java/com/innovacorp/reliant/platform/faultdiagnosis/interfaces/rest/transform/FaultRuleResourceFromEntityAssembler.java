package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.transform;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources.FaultRuleResource;

public class FaultRuleResourceFromEntityAssembler {
    public static FaultRuleResource toResourceFromEntity(FaultRule r) {
        return new FaultRuleResource(r.getId(), r.getOrganizationIdValue(), r.getParameter(), r.getBand().name().toLowerCase(),
                r.getProbableCause(), r.getRecommendedAction(), r.getCooldownSeconds());
    }
}
