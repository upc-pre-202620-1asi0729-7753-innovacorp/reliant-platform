package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.StartSpraySessionCommand;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.StartSpraySessionResource;

public class StartSpraySessionCommandFromResourceAssembler {
    public static StartSpraySessionCommand toCommandFromResource(StartSpraySessionResource resource) {
        return new StartSpraySessionCommand(resource.hvofSystemId(), resource.recuperationId(), resource.recipeNumber(), resource.timeZone());
    }
}
