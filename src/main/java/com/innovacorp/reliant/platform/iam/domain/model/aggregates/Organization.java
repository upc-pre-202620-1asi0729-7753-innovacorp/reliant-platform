package com.innovacorp.reliant.platform.iam.domain.model.aggregates;

import com.innovacorp.reliant.platform.iam.domain.model.events.OrganizationRegisteredEvent;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.OrganizationStatus;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.OrganizationType;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Organization aggregate root: a recuperation supplier (e.g. FESA) or an asset owner (a mine).
 */
@Getter
public class Organization extends AbstractDomainAggregateRoot<Organization> {

    @Setter
    private Long id;
    private final String legalName;
    private final Ruc ruc;
    private final OrganizationType organizationType;
    private OrganizationStatus status;

    public Organization(Long id, String legalName, Ruc ruc, OrganizationType organizationType, OrganizationStatus status) {
        if (legalName == null || legalName.isBlank()) {
            throw new IllegalArgumentException("Organization legal name must not be blank");
        }
        this.id = id;
        this.legalName = legalName.trim();
        this.ruc = Objects.requireNonNull(ruc, "ruc must not be null");
        this.organizationType = Objects.requireNonNull(organizationType, "organizationType must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public Organization(String legalName, Ruc ruc, OrganizationType organizationType) {
        this(null, legalName, ruc, organizationType, OrganizationStatus.ACTIVE);
    }

    public static Organization reconstitute(Long id, String legalName, Ruc ruc, OrganizationType organizationType, OrganizationStatus status) {
        return new Organization(id, legalName, ruc, organizationType, status);
    }

    public String getRucValue() {
        return ruc.ruc();
    }

    public boolean isSupplier() {
        return organizationType == OrganizationType.RECUPERATION_SUPPLIER;
    }

    public void onCreated() {
        registerDomainEvent(OrganizationRegisteredEvent.from(this));
    }
}
