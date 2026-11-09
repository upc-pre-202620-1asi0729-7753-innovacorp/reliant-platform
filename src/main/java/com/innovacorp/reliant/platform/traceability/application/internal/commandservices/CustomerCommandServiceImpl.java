package com.innovacorp.reliant.platform.traceability.application.internal.commandservices;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.application.commandservices.CustomerCommandService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerCommandServiceImpl implements CustomerCommandService {

    private final CustomerRepository customerRepository;
    private final CurrentUserProvider currentUserProvider;

    public CustomerCommandServiceImpl(CustomerRepository customerRepository, CurrentUserProvider currentUserProvider) {
        this.customerRepository = customerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result<Customer, ApplicationError> handle(RegisterCustomerCommand command) {
        var currentUser = currentUserProvider.current();
        if (!currentUser.isSupplier()) {
            return Result.failure(forbidden());
        }

        try {
            var supplierOrganizationId = new OrganizationId(currentUser.organizationId());
            var ruc = new Ruc(command.ruc());
            if (customerRepository.existsBySupplierOrganizationIdAndRuc(supplierOrganizationId, ruc)) {
                return Result.failure(ApplicationError.conflict("customer", command.ruc()));
            }

            var customer = new Customer(new RegisterCustomerCommand(
                    currentUser.organizationId(), command.linkedAssetOwnerOrganizationId(),
                    command.legalName(), command.ruc(), command.mineSite()));
            return Result.success(customerRepository.save(customer));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Customer", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Customer registration", e.getMessage()));
        }
    }

    @Override
    public Result<Customer, ApplicationError> handle(UpdateCustomerCommand command) {
        var currentUser = currentUserProvider.current();
        if (!currentUser.isSupplier()) {
            return Result.failure(forbidden());
        }

        try {
            var customer = customerRepository.findById(command.customerId())
                    .filter(existing -> existing.isOwnedBy(currentUser.organizationId()));
            if (customer.isEmpty()) {
                return Result.failure(ApplicationError.notFound("customer", String.valueOf(command.customerId())));
            }

            var customerToUpdate = customer.get();
            customerToUpdate.update(command);
            return Result.success(customerRepository.save(customerToUpdate));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Customer", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Customer update", e.getMessage()));
        }
    }

    private static ApplicationError forbidden() {
        return new ApplicationError(
                "ACCESS_FORBIDDEN",
                "Access forbidden",
                "Only the supplier organization can manage its customers");
    }
}