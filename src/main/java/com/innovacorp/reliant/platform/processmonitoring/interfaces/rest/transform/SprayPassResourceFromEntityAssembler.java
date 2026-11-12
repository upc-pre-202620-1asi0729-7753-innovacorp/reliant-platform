package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.SprayPassResource;

public class SprayPassResourceFromEntityAssembler {
    public static SprayPassResource toResourceFromEntity(SprayPass p, Long spraySessionId) {
        return new SprayPassResource(p.getId(), spraySessionId, p.getPassNumber(), p.getStartedAt(), p.getEndedAt());
    }
}
