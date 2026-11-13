package com.innovacorp.reliant.platform.traceability.application.internal.commandservices;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.traceability.application.commandservices.RecoveredComponentCommandService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterComponentCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecoveredComponentRepository;
import org.springframework.stereotype.Service;

@Service
public class RecoveredComponentCommandServiceImpl implements RecoveredComponentCommandService {
    private final RecoveredComponentRepository componentRepository;
    private final CustomerRepository customerRepository;
    private final CurrentUserProvider currentUserProvider;

    public RecoveredComponentCommandServiceImpl(RecoveredComponentRepository componentRepository, CustomerRepository customerRepository, CurrentUserProvider currentUserProvider) {
        this.componentRepository = componentRepository;
        this.customerRepository = customerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result<RecoveredComponent, ApplicationError> handle(RegisterComponentCommand command) {
        var currentUser = currentUserProvider.current();
        var customer = customerRepository.findById(command.customerId());
        if (customer.isEmpty()) {
            return Result.failure(ApplicationError.notFound("customer", String.valueOf(command.customerId())));
        }
        if (!currentUser.isSupplier() || !customer.get().isOwnedBy(currentUser.organizationId())) {
            return Result.failure(new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only the supplier of the customer can register components"));
        }
        try {
            var customerId = new CustomerId(command.customerId());
            if (componentRepository.existsByCustomerIdAndSerialNumber(customerId, command.serialNumber().trim())) {
                return Result.failure(ApplicationError.conflict("component", command.serialNumber()));
            }
            return Result.success(componentRepository.save(new RecoveredComponent(command)));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Component", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Component registration", e.getMessage()));
        }
    }
}
