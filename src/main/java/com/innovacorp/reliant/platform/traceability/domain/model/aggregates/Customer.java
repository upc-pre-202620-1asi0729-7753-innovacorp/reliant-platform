package com.innovacorp.reliant.platform.traceability.domain.model.aggregates;

import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.events.CustomerRegisteredEvent;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import lombok.Getter;

import java.util.Objects;

/**
 * Customer aggregate root: a mining company served by a recuperation supplier.
 */
@Getter
public class Customer extends AbstractDomainAggregateRoot<Customer> {

    private Long id;
    private final OrganizationId supplierOrganizationId;
    private Long linkedAssetOwnerOrganizationId;
    private String legalName;
    private final Ruc ruc;
    private String mineSite;

    public Customer(Long id, OrganizationId supplierOrganizationId, Long linkedAssetOwnerOrganizationId, String legalName, Ruc ruc, String mineSite) {
        this.id = id;
        this.supplierOrganizationId = Objects.requireNonNull(supplierOrganizationId, "supplierOrganizationId must not be null");
        this.linkedAssetOwnerOrganizationId = linkedAssetOwnerOrganizationId;
        this.legalName = requireText(legalName, "validation.customer.legal-name.blank");
        this.ruc = Objects.requireNonNull(ruc, "ruc must not be null");
        this.mineSite = requireText(mineSite, "validation.customer.mine-site.blank");
    }

    public Customer(RegisterCustomerCommand command) {
        this(null, new OrganizationId(command.supplierOrganizationId()), command.linkedAssetOwnerOrganizationId(),
                command.legalName(), new Ruc(command.ruc()), command.mineSite());
    }

    public static Customer reconstitute(Long id, OrganizationId supplierOrganizationId, Long linkedAssetOwnerOrganizationId, String legalName, Ruc ruc, String mineSite) {
        return new Customer(id, supplierOrganizationId, linkedAssetOwnerOrganizationId, legalName, ruc, mineSite);
    }

    public void update(UpdateCustomerCommand command) {
        this.legalName = requireText(command.legalName(), "validation.customer.legal-name.blank");
        this.mineSite = requireText(command.mineSite(), "validation.customer.mine-site.blank");
        this.linkedAssetOwnerOrganizationId = command.linkedAssetOwnerOrganizationId();
    }

    /** Links the customer to the asset-owner organization that signed up with its RUC. Idempotent. */
    public void linkAssetOwner(Long assetOwnerOrganizationId) {
        this.linkedAssetOwnerOrganizationId = Objects.requireNonNull(assetOwnerOrganizationId, "assetOwnerOrganizationId must not be null");
    }

    public boolean isLinked() {
        return linkedAssetOwnerOrganizationId != null;
    }

    public boolean isOwnedBy(Long organizationId) {
        return supplierOrganizationId.organizationId().equals(organizationId);
    }

    public boolean isLinkedTo(Long assetOwnerOrganizationId) {
        return linkedAssetOwnerOrganizationId != null && linkedAssetOwnerOrganizationId.equals(assetOwnerOrganizationId);
    }

    public Long getSupplierOrganizationIdValue() { return supplierOrganizationId.organizationId(); }

    public String getRucValue() { return ruc.ruc(); }

    public void onCreated() { registerDomainEvent(CustomerRegisteredEvent.from(this)); }

    private static String requireText(String value, String messageKey) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(messageKey);
        return value.trim();
    }
}