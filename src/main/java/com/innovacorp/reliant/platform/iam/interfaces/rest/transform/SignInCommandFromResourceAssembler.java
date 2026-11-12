package com.innovacorp.reliant.platform.iam.interfaces.rest.transform;

import com.innovacorp.reliant.platform.iam.domain.model.commands.SignInCommand;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }
}
