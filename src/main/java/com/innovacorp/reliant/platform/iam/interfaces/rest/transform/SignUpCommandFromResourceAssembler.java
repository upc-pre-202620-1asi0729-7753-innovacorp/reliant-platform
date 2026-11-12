package com.innovacorp.reliant.platform.iam.interfaces.rest.transform;

import com.innovacorp.reliant.platform.iam.domain.model.commands.SignUpCommand;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(
                resource.organizationName(),
                resource.ruc(),
                resource.organizationType(),
                resource.fullName(),
                resource.email(),
                resource.password());
    }
}
