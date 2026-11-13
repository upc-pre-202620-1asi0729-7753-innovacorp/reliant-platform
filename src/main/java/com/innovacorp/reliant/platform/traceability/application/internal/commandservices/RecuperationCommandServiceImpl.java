package com.innovacorp.reliant.platform.traceability.application.internal.commandservices;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.application.commandservices.RecuperationCommandService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.CompleteRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkSessionToRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.OpenRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecoveredComponentRepository;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecuperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecuperationCommandServiceImpl implements RecuperationCommandService {
    private final RecuperationRepository recuperationRepository;
    private final CustomerRepository customerRepository;
    private final RecoveredComponentRepository componentRepository;
    private final CurrentUserProvider currentUserProvider;

    public RecuperationCommandServiceImpl(RecuperationRepository recuperationRepository, CustomerRepository customerRepository,
                                          RecoveredComponentRepository componentRepository, CurrentUserProvider currentUserProvider) {
        this.recuperationRepository = recuperationRepository;
        this.customerRepository = customerRepository;
        this.componentRepository = componentRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @Transactional
    public Result<Recuperation, ApplicationError> handle(OpenRecuperationCommand command) {
        var currentUser = currentUserProvider.current();
        var customer = customerRepository.findById(command.customerId());
        if (customer.isEmpty()) {
            return Result.failure(ApplicationError.notFound("customer", String.valueOf(command.customerId())));
        }
        if (!currentUser.isSupplier() || !customer.get().isOwnedBy(currentUser.organizationId())) {
            return Result.failure(new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only the supplier of the customer can open recuperations"));
        }
        var component = componentRepository.findById(command.componentId());
        if (component.isEmpty() || !component.get().getCustomerIdValue().equals(command.customerId())) {
            return Result.failure(ApplicationError.notFound("component", String.valueOf(command.componentId())));
        }
        try {
            var supplierOrganizationId = new OrganizationId(currentUser.organizationId());
            var workOrderNumber = new WorkOrderNumber(command.workOrderNumber());
            if (recuperationRepository.existsBySupplierOrganizationIdAndWorkOrderNumber(supplierOrganizationId, workOrderNumber)) {
                return Result.failure(ApplicationError.conflict("recuperation", command.workOrderNumber()));
            }
            component.get().markInRecuperation();
            componentRepository.save(component.get());
            return Result.success(recuperationRepository.save(new Recuperation(supplierOrganizationId, command)));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Recuperation", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Recuperation opening", e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<Recuperation, ApplicationError> handle(CompleteRecuperationCommand command) {
        var currentUser = currentUserProvider.current();
        var recuperation = recuperationRepository.findById(command.recuperationId());
        if (recuperation.isEmpty()) {
            return Result.failure(ApplicationError.notFound("recuperation", String.valueOf(command.recuperationId())));
        }
        if (!recuperation.get().isOwnedBy(currentUser.organizationId())) {
            return Result.failure(new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only the owning supplier can complete the recuperation"));
        }
        try {
            recuperation.get().complete();
            componentRepository.findById(recuperation.get().getComponentIdValue()).ifPresent(c -> {
                c.markInService();
                componentRepository.save(c);
            });
            return Result.success(recuperationRepository.save(recuperation.get()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("recuperation-completion", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Recuperation completion", e.getMessage()));
        }
    }

    /** Called by the ACL facade from Process Monitoring; no user check (the caller already validated the tenant). */
    @Override
    public Result<Recuperation, ApplicationError> handle(LinkSessionToRecuperationCommand command) {
        var recuperation = recuperationRepository.findById(command.recuperationId());
        if (recuperation.isEmpty()) {
            return Result.failure(ApplicationError.notFound("recuperation", String.valueOf(command.recuperationId())));
        }
        recuperation.get().linkSession(command.sessionId());
        return Result.success(recuperationRepository.save(recuperation.get()));
    }
}
