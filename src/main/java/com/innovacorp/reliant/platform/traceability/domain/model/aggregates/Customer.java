package com.innovacorp.reliant.platform.traceability.domain.model.aggregates;

import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.UpdateCustomerCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.events.CustomerRegisteredEvent;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Customer aggregate root for a mining company served by a recuperation supplier.
 */
@Getter
public class Customer extends AbstractDomainAggregateRoot<Customer> {

    @Setter
    private Long id;
    private final OrganizationId supplierOrganizationId;
    private Long linkedAssetOwnerOrganizationId;
    private String legalName;
    private final Ruc ruc;
    private String mineSite;

    public Customer(Long id, OrganizationId supplierOrganizationId, Long linkedAssetOwnerOrganizationId,
                    String legalName, Ruc ruc, String mineSite) {
        this.id = id;
        this.supplierOrganizationId = Objects.requireNonNull(supplierOrganizationId, "supplierOrganizationId must not be null");
        this.linkedAssetOwnerOrganizationId = linkedAssetOwnerOrganizationId;
        this.legalName = requireText(legalName, "Legal name");
        this.ruc = Objects.requireNonNull(ruc, "ruc must not be null");
        this.mineSite = requireText(mineSite, "Mine site");
    }

    public Customer(RegisterCustomerCommand command) {
        this(null, new OrganizationId(command.supplierOrganizationId()), command.linkedAssetOwnerOrganizationId(),
                command.legalName(), new Ruc(command.ruc()), command.mineSite());
    }

    public static Customer reconstitute(Long id, OrganizationId supplierOrganizationId,
                                        Long linkedAssetOwnerOrganizationId, String legalName, Ruc ruc, String mineSite) {
        return new Customer(id, supplierOrganizationId, linkedAssetOwnerOrganizationId, legalName, ruc, mineSite);
    }

    public void update(UpdateCustomerCommand command) {
        this.legalName = requireText(command.legalName(), "Legal name");
        this.mineSite = requireText(command.mineSite(), "Mine site");
        this.linkedAssetOwnerOrganizationId = command.linkedAssetOwnerOrganizationId();
    }

    public boolean isOwnedBy(Long organizationId) {
        return supplierOrganizationId.organizationId().equals(organizationId);
    }

    public boolean isLinkedTo(Long assetOwnerOrganizationId) {
        return linkedAssetOwnerOrganizationId != null && linkedAssetOwnerOrganizationId.equals(assetOwnerOrganizationId);
    }

    public Long getSupplierOrganizationIdValue() {
        return supplierOrganizationId.organizationId();
    }

    public String getRucValue() {
        return ruc.ruc();
    }

    public void onCreated() {
        registerDomainEvent(CustomerRegisteredEvent.from(this));
    }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be null or blank");
        }
        return value.trim();
    }
}