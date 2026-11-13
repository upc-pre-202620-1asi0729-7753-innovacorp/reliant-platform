package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.RegisterSprayPassCommand;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.RegisterSprayPassResource;

public class RegisterSprayPassCommandFromResourceAssembler {
    public static RegisterSprayPassCommand toCommandFromResource(Long spraySessionId, RegisterSprayPassResource resource) {
        return new RegisterSprayPassCommand(spraySessionId, resource.passNumber(), resource.startedAt(), resource.endedAt());
    }
}
