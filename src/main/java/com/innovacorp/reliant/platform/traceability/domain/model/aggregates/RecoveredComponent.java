package com.innovacorp.reliant.platform.traceability.domain.model.aggregates;

import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.traceability.domain.model.commands.RegisterComponentCommand;
import com.innovacorp.reliant.platform.traceability.domain.model.events.ComponentRegisteredEvent;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentStatus;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentType;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Recovered component aggregate root: a customer's part that goes through HVOF recuperation cycles.
 */
@Getter
public class RecoveredComponent extends AbstractDomainAggregateRoot<RecoveredComponent> {

    @Setter
    private Long id;
    private final CustomerId customerId;
    private final String serialNumber;
    private final String partNumber;
    private final ComponentType componentType;
    private final String machineManufacturer;
    private final String machineModel;
    private final Integer pcrTargetHours;
    private ComponentStatus status;

    public RecoveredComponent(Long id, CustomerId customerId, String serialNumber, String partNumber, ComponentType componentType,
                              String machineManufacturer, String machineModel, Integer pcrTargetHours, ComponentStatus status) {
        if (pcrTargetHours == null || pcrTargetHours <= 0) throw new IllegalArgumentException("PCR target hours must be positive");
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        this.serialNumber = requireText(serialNumber, "Serial number");
        this.partNumber = requireText(partNumber, "Part number");
        this.componentType = Objects.requireNonNull(componentType, "componentType must not be null");
        this.machineManufacturer = requireText(machineManufacturer, "Machine manufacturer");
        this.machineModel = requireText(machineModel, "Machine model");
        this.pcrTargetHours = pcrTargetHours;
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public RecoveredComponent(RegisterComponentCommand command) {
        this(null, new CustomerId(command.customerId()), command.serialNumber(), command.partNumber(), ComponentType.fromString(command.componentType()),
             command.machineManufacturer(), command.machineModel(), command.pcrTargetHours(), ComponentStatus.RECEIVED);
    }

    public static RecoveredComponent reconstitute(Long id, CustomerId customerId, String serialNumber, String partNumber, ComponentType componentType,
                                                  String machineManufacturer, String machineModel, Integer pcrTargetHours, ComponentStatus status) {
        return new RecoveredComponent(id, customerId, serialNumber, partNumber, componentType, machineManufacturer, machineModel, pcrTargetHours, status);
    }

    public void markInRecuperation() { status = ComponentStatus.IN_RECUPERATION; }

    public void markInService() { status = ComponentStatus.IN_SERVICE; }

    public Long getCustomerIdValue() { return customerId.customerId(); }

    public void onCreated() { registerDomainEvent(ComponentRegisteredEvent.from(this)); }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be null or blank");
        return value.trim();
    }
}
