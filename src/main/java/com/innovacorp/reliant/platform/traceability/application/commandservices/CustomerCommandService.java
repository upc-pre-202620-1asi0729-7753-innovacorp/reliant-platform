package com.innovacorp.reliant.platform.traceability.application.commandservices;

import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkAssetOwnerToCustomersCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;

import java.util.List;

public interface CustomerCommandService {
    Result<Customer, ApplicationError> handle(RegisterCustomerCommand command);

    Result<Customer, ApplicationError> handle(UpdateCustomerCommand command);

    /** Reaction to OrganizationRegisteredIntegrationEvent; returns the customers that got linked. */
    Result<List<Customer>, ApplicationError> handle(LinkAssetOwnerToCustomersCommand command);
}
