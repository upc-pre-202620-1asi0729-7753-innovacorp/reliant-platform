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
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecoveredComponentRepository;
import com.innovacorp.reliant.platform.traceability.domain.repositories.RecuperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecuperationCommandServiceImpl implements RecuperationCommandService {

    private final RecuperationRepository recuperationRepository;
    private final CustomerRepository customerRepository;
    private final RecoveredComponentRepository componentRepository;
    private final CurrentUserProvider currentUserProvider;

    public RecuperationCommandServiceImpl(RecuperationRepository recuperationRepository,
                                          CustomerRepository customerRepository,
                                          RecoveredComponentRepository componentRepository,
                                          CurrentUserProvider currentUserProvider) {
        this.recuperationRepository = recuperationRepository;
        this.customerRepository = customerRepository;
        this.componentRepository = componentRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result<Recuperation, ApplicationError> handle(OpenRecuperationCommand command) {
        var currentUser = currentUserProvider.current();
        if (!currentUser.isSupplier()) {
            return Result.failure(forbidden());
        }

        var supplierOrganizationId = new OrganizationId(currentUser.organizationId());
        Recuperation recuperation;
        try {
            recuperation = new Recuperation(supplierOrganizationId, command);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Recuperation", e.getMessage()));
        }

        var customer = customerRepository.findById(command.customerId())
                .filter(existing -> existing.isOwnedBy(currentUser.organizationId()));
        if (customer.isEmpty()) {
            return Result.failure(ApplicationError.notFound("customer", String.valueOf(command.customerId())));
        }

        var component = componentRepository.findById(command.componentId())
                .filter(existing -> existing.getCustomerIdValue().equals(command.customerId()));
        if (component.isEmpty()) {
            return Result.failure(ApplicationError.notFound("component", String.valueOf(command.componentId())));
        }

        if (recuperationRepository.existsBySupplierOrganizationIdAndWorkOrderNumber(
                supplierOrganizationId, recuperation.getWorkOrderNumber())) {
            return Result.failure(ApplicationError.conflict("recuperation", command.workOrderNumber()));
        }

        var componentToUpdate = component.get();
        componentToUpdate.markInRecuperation();
        componentRepository.save(componentToUpdate);
        return Result.success(recuperationRepository.save(recuperation));
    }

    @Override
    public Result<Recuperation, ApplicationError> handle(CompleteRecuperationCommand command) {
        var currentUser = currentUserProvider.current();
        if (!currentUser.isSupplier()) {
            return Result.failure(forbidden());
        }

        var recuperation = recuperationRepository.findById(command.recuperationId())
                .filter(existing -> existing.isOwnedBy(currentUser.organizationId()));
        if (recuperation.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "recuperation", String.valueOf(command.recuperationId())));
        }

        var recuperationToComplete = recuperation.get();
        var component = componentRepository.findById(recuperationToComplete.getComponentIdValue())
                .filter(existing -> existing.getCustomerIdValue()
                        .equals(recuperationToComplete.getCustomerIdValue()));
        if (component.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "component", recuperationToComplete.getComponentIdValue().toString()));
        }

        try {
            recuperationToComplete.complete();
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "recuperation-completion", e.getMessage()));
        }

        var componentToUpdate = component.get();
        componentToUpdate.markInService();
        componentRepository.save(componentToUpdate);
        return Result.success(recuperationRepository.save(recuperationToComplete));
    }

    /**
     * Called through the ACL after Process Monitoring validates the tenant.
     */
    @Override
    public Result<Recuperation, ApplicationError> handle(LinkSessionToRecuperationCommand command) {
        if (command.sessionId() == null || command.sessionId() <= 0) {
            return Result.failure(ApplicationError.validationError(
                    "Session id", "Session id must be a positive number"));
        }

        var recuperation = recuperationRepository.findById(command.recuperationId());
        if (recuperation.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "recuperation", String.valueOf(command.recuperationId())));
        }

        var recuperationToUpdate = recuperation.get();
        recuperationToUpdate.linkSession(command.sessionId());
        return Result.success(recuperationRepository.save(recuperationToUpdate));
    }

    private static ApplicationError forbidden() {
        return new ApplicationError(
                "ACCESS_FORBIDDEN",
                "Access forbidden",
                "Only suppliers can manage recuperations");
    }
}