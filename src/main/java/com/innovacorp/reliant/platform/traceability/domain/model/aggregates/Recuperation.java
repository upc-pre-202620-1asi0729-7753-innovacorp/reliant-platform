package com.innovacorp.reliant.platform.traceability.domain.model.aggregates;

import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.OpenRecuperationCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.events.RecuperationCompletedEvent;
import com.innovacorp.reliant.platform.traceability.domain.model.events.RecuperationOpenedEvent;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.RecuperationStatus;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Recuperation aggregate root: one HVOF recuperation order of a component. Spray sessions link to it by id.
 */
@Getter
public class Recuperation extends AbstractDomainAggregateRoot<Recuperation> {

    @Setter
    private Long id;
    private final OrganizationId supplierOrganizationId;
    private final WorkOrderNumber workOrderNumber;
    private final String manufacturingOrderNumber;
    private final ComponentId componentId;
    private final CustomerId customerId;
    private final String segment;
    private final String operation;
    private final Double weightValue;
    private final String weightUnitSymbol;
    private final Integer hourmeterAtEntry;
    private final String powderSupplier;
    private final String powderLotNumber;
    private final String powderChemicalComposition;
    private RecuperationStatus status;
    private final LocalDate receivedAt;
    private LocalDate completedAt;
    private final List<Long> linkedSessionIds;

    public Recuperation(Long id, OrganizationId supplierOrganizationId, WorkOrderNumber workOrderNumber, String manufacturingOrderNumber, ComponentId componentId,
                        CustomerId customerId, String segment, String operation, Double weightValue, String weightUnitSymbol, Integer hourmeterAtEntry,
                        String powderSupplier, String powderLotNumber, String powderChemicalComposition, RecuperationStatus status,
                        LocalDate receivedAt, LocalDate completedAt, List<Long> linkedSessionIds) {
        if (weightValue == null || weightValue <= 0) throw new IllegalArgumentException("Weight must be positive");
        if (hourmeterAtEntry == null || hourmeterAtEntry < 0) throw new IllegalArgumentException("Hourmeter at entry must be zero or positive");
        this.id = id;
        this.supplierOrganizationId = Objects.requireNonNull(supplierOrganizationId, "supplierOrganizationId must not be null");
        this.workOrderNumber = Objects.requireNonNull(workOrderNumber, "workOrderNumber must not be null");
        this.manufacturingOrderNumber = requireText(manufacturingOrderNumber, "Manufacturing order number");
        this.componentId = Objects.requireNonNull(componentId, "componentId must not be null");
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        this.segment = segment == null || segment.isBlank() ? "Mining" : segment.trim();
        this.operation = requireText(operation, "Operation");
        this.weightValue = weightValue;
        this.weightUnitSymbol = weightUnitSymbol == null || weightUnitSymbol.isBlank() ? "kg" : weightUnitSymbol.trim();
        this.hourmeterAtEntry = hourmeterAtEntry;
        this.powderSupplier = requireText(powderSupplier, "Powder supplier");
        this.powderLotNumber = requireText(powderLotNumber, "Powder lot number");
        this.powderChemicalComposition = requireText(powderChemicalComposition, "Powder chemical composition");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.receivedAt = receivedAt == null ? LocalDate.now() : receivedAt;
        this.completedAt = completedAt;
        this.linkedSessionIds = new ArrayList<>(linkedSessionIds == null ? List.of() : linkedSessionIds);
    }

    public Recuperation(OrganizationId supplierOrganizationId, OpenRecuperationCommand c) {
        this(null, supplierOrganizationId, new WorkOrderNumber(c.workOrderNumber()), c.manufacturingOrderNumber(), new ComponentId(c.componentId()),
             new CustomerId(c.customerId()), c.segment(), c.operation(), c.weightValue(), c.weightUnitSymbol(), c.hourmeterAtEntry(),
             c.powderSupplier(), c.powderLotNumber(), c.powderChemicalComposition(), RecuperationStatus.RECEIVED, c.receivedAt(), null, List.of());
    }

    public static Recuperation reconstitute(Long id, OrganizationId supplierOrganizationId, WorkOrderNumber workOrderNumber, String manufacturingOrderNumber,
                                            ComponentId componentId, CustomerId customerId, String segment, String operation, Double weightValue, String weightUnitSymbol,
                                            Integer hourmeterAtEntry, String powderSupplier, String powderLotNumber, String powderChemicalComposition,
                                            RecuperationStatus status, LocalDate receivedAt, LocalDate completedAt, List<Long> linkedSessionIds) {
        return new Recuperation(id, supplierOrganizationId, workOrderNumber, manufacturingOrderNumber, componentId, customerId, segment, operation, weightValue,
                weightUnitSymbol, hourmeterAtEntry, powderSupplier, powderLotNumber, powderChemicalComposition, status, receivedAt, completedAt, linkedSessionIds);
    }

    /** Linking the first spray session moves RECEIVED → IN_PROGRESS. Idempotent per session id. */
    public void linkSession(Long sessionId) {
        if (!linkedSessionIds.contains(sessionId)) linkedSessionIds.add(sessionId);
        if (status == RecuperationStatus.RECEIVED) status = RecuperationStatus.IN_PROGRESS;
    }

    /** IN_PROGRESS | REWORK → COMPLETED. IllegalStateException otherwise (422). */
    public void complete() {
        if (status != RecuperationStatus.IN_PROGRESS && status != RecuperationStatus.REWORK) {
            throw new IllegalStateException("Only recuperations in progress or in rework can be completed");
        }
        status = RecuperationStatus.COMPLETED;
        completedAt = LocalDate.now();
        registerDomainEvent(RecuperationCompletedEvent.from(this));
    }

    public boolean isOwnedBy(Long organizationId) { return supplierOrganizationId.organizationId().equals(organizationId); }

    public Long getSupplierOrganizationIdValue() { return supplierOrganizationId.organizationId(); }

    public String getWorkOrderNumberValue() { return workOrderNumber.workOrderNumber(); }

    public Long getComponentIdValue() { return componentId.componentId(); }

    public Long getCustomerIdValue() { return customerId.customerId(); }

    public void onCreated() { registerDomainEvent(RecuperationOpenedEvent.from(this)); }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be null or blank");
        return value.trim();
    }
}
