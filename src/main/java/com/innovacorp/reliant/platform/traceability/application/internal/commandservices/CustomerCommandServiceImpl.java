package com.innovacorp.reliant.platform.traceability.application.internal.commandservices;

import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.application.commandservices.CustomerCommandService;
import com.innovacorp.reliant.platform.traceability.application.internal.outboundservices.acl.ExternalIamService;
import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Customer;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.LinkAssetOwnerToCustomersCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.traceability.domain.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Customer Command Service Implementation. The supplier organization comes from the authenticated user; the linked
 * asset-owner organization is resolved through the IAM anti-corruption layer by the customer's RUC.
 */
@Service
public class CustomerCommandServiceImpl implements CustomerCommandService {
    private final CustomerRepository customerRepository;
    private final ExternalIamService externalIamService;
    private final CurrentUserProvider currentUserProvider;

    public CustomerCommandServiceImpl(CustomerRepository customerRepository, ExternalIamService externalIamService, CurrentUserProvider currentUserProvider) {
        this.customerRepository = customerRepository;
        this.externalIamService = externalIamService;
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
            var linkedAssetOwnerOrganizationId = command.linkedAssetOwnerOrganizationId() != null
                    ? command.linkedAssetOwnerOrganizationId()
                    : externalIamService.fetchAssetOwnerOrganizationIdByRuc(command.ruc()).orElse(null);
            var customer = new Customer(new RegisterCustomerCommand(currentUser.organizationId(), linkedAssetOwnerOrganizationId,
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
        var customer = customerRepository.findById(command.customerId());
        if (customer.isEmpty()) {
            return Result.failure(ApplicationError.notFound("customer", String.valueOf(command.customerId())));
        }
        if (!currentUser.isSupplier() || !customer.get().isOwnedBy(currentUser.organizationId())) {
            return Result.failure(forbidden());
        }
        try {
            var linkedAssetOwnerOrganizationId = command.linkedAssetOwnerOrganizationId() != null
                    ? command.linkedAssetOwnerOrganizationId()
                    : externalIamService.fetchAssetOwnerOrganizationIdByRuc(customer.get().getRucValue()).orElse(null);
            customer.get().update(new UpdateCustomerCommand(command.customerId(), linkedAssetOwnerOrganizationId, command.legalName(), command.mineSite()));
            return Result.success(customerRepository.save(customer.get()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Customer", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Customer update", e.getMessage()));
        }
    }

    @Override
    public Result<List<Customer>, ApplicationError> handle(LinkAssetOwnerToCustomersCommand command) {
        try {
            var linked = customerRepository.findAllByRuc(new Ruc(command.ruc())).stream()
                    .filter(customer -> !customer.isLinkedTo(command.assetOwnerOrganizationId()))
                    .map(customer -> {
                        customer.linkAssetOwner(command.assetOwnerOrganizationId());
                        return customerRepository.save(customer);
                    })
                    .toList();
            return Result.success(linked);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Customer", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Asset owner linking", e.getMessage()));
        }
    }

    private static ApplicationError forbidden() {
        return new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only the supplier organization can manage its customers");
    }
}
