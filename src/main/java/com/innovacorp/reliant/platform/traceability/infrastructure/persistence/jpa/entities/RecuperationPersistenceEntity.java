package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.RecuperationStatus;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters.ComponentIdPersistenceConverter;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters.CustomerIdPersistenceConverter;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters.WorkOrderNumberPersistenceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recuperations", uniqueConstraints = @UniqueConstraint(
        name = "uk_recuperation_supplier_work_order",
        columnNames = {"supplier_organization_id", "work_order_number"}))
@Getter
@Setter
@NoArgsConstructor
public class RecuperationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "supplier_organization_id", nullable = false)
    private OrganizationId supplierOrganizationId;

    @Convert(converter = WorkOrderNumberPersistenceConverter.class)
    @Column(name = "work_order_number", nullable = false, length = 30)
    private WorkOrderNumber workOrderNumber;

    @Column(name = "manufacturing_order_number", nullable = false, length = 30)
    private String manufacturingOrderNumber;

    @Convert(converter = ComponentIdPersistenceConverter.class)
    @Column(name = "component_id", nullable = false)
    private ComponentId componentId;

    @Convert(converter = CustomerIdPersistenceConverter.class)
    @Column(name = "customer_id", nullable = false)
    private CustomerId customerId;

    @Column(name = "segment", nullable = false, length = 40)
    private String segment;

    @Column(name = "operation", nullable = false, length = 120)
    private String operation;

    @Column(name = "weight_value", nullable = false)
    private Double weightValue;

    @Column(name = "weight_unit_symbol", nullable = false, length = 10)
    private String weightUnitSymbol;

    @Column(name = "hourmeter_at_entry", nullable = false)
    private Integer hourmeterAtEntry;

    @Column(name = "powder_supplier", nullable = false, length = 80)
    private String powderSupplier;

    @Column(name = "powder_lot_number", nullable = false, length = 40)
    private String powderLotNumber;

    @Column(name = "powder_chemical_composition", nullable = false, length = 40)
    private String powderChemicalComposition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecuperationStatus status;

    @Column(name = "received_at", nullable = false)
    private LocalDate receivedAt;

    @Column(name = "completed_at")
    private LocalDate completedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "recuperation_linked_sessions",
            joinColumns = @JoinColumn(name = "recuperation_id"))
    @Column(name = "session_id", nullable = false)
    private List<Long> linkedSessionIds = new ArrayList<>();
}