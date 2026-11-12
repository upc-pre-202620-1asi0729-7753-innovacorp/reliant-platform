package com.innovacorp.reliant.platform.iam.application.commandservices;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.domain.model.commands.SignUpCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

/**
 * User Command Service
 */
public interface UserCommandService {
    Result<User, ApplicationError> handle(SignUpCommand command);
}
