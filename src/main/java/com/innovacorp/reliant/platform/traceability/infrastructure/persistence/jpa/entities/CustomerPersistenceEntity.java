package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters.RucPersistenceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(
        name = "uk_customer_supplier_ruc", columnNames = {"supplier_organization_id", "ruc"}))
@Getter
@Setter
@NoArgsConstructor
public class CustomerPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "supplier_organization_id", nullable = false)
    private OrganizationId supplierOrganizationId;

    @Column(name = "linked_asset_owner_organization_id")
    private Long linkedAssetOwnerOrganizationId;

    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;

    @Convert(converter = RucPersistenceConverter.class)
    @Column(name = "ruc", nullable = false, length = 11)
    private Ruc ruc;

    @Column(name = "mine_site", nullable = false, length = 120)
    private String mineSite;
}