package com.innovacorp.reliant.platform.iam.application.commandservices;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.commands.SignInCommand;
import com.innovacorp.reliant.platform.iam.domain.model.commands.SignUpCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;
import com.innovacorp.reliant.platform.iam.domain.model.commands.AssignRolesToUserCommand;

public interface UserCommandService {

    Result<User, ApplicationError> handle(SignUpCommand command);

    Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command);
    Result<User, ApplicationError> handle(AssignRolesToUserCommand command);
}

