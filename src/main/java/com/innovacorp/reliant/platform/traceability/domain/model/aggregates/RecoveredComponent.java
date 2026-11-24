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
        if (pcrTargetHours == null || pcrTargetHours <= 0) throw new IllegalArgumentException("validation.component.pcr-target-hours.positive");
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        this.serialNumber = requireText(serialNumber, "validation.component.serial-number.blank");
        this.partNumber = requireText(partNumber, "validation.component.part-number.blank");
        this.componentType = Objects.requireNonNull(componentType, "componentType must not be null");
        this.machineManufacturer = requireText(machineManufacturer, "validation.component.machine-manufacturer.blank");
        this.machineModel = requireText(machineModel, "validation.component.machine-model.blank");
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

    private static String requireText(String value, String messageKey) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(messageKey);
        return value.trim();
    }
}