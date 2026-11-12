package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.SprayPassResource;

public class SprayPassResourceFromEntityAssembler {

    public static SprayPassResource toResourceFromEntity(SprayPass pass, Long spraySessionId) {
        return new SprayPassResource(
                pass.getId(), spraySessionId, pass.getPassNumber(), pass.getStartedAt(), pass.getEndedAt());
    }
}