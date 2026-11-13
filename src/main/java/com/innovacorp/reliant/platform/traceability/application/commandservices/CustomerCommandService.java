package com.innovacorp.reliant.platform.traceability.application.commandservices;

import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;

public interface CustomerCommandService {
    Result<Customer, ApplicationError> handle(RegisterCustomerCommand command);

    Result<Customer, ApplicationError> handle(UpdateCustomerCommand command);
}
