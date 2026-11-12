package com.innovacorp.reliant.platform.equipment.domain.model.aggregates;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.equipment.domain.model.events.HvofSystemRegisteredEvent;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.*;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class HvofSystem extends AbstractDomainAggregateRoot<HvofSystem> {

    @Setter
    private Long id;
    private final OrganizationId organizationId;
    private final HvofSystemCode code;
    private final String serialNumber;
    private final String systemManufacturer;
    private final String systemModel;
    private final FuelType fuelType;
    private HvofSystemStatus status;
    private final List<HvofSubsystem> subsystems;
    private final List<Controller> controllers;

    public HvofSystem(Long id, OrganizationId organizationId, HvofSystemCode code, String serialNumber,
                      String systemManufacturer, String systemModel, FuelType fuelType, HvofSystemStatus status,
                      List<HvofSubsystem> subsystems, List<Controller> controllers) {
        this.id = id;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.serialNumber = requireText(serialNumber, "Serial number");
        this.systemManufacturer = requireText(systemManufacturer, "System manufacturer");
        this.systemModel = requireText(systemModel, "System model");
        this.fuelType = Objects.requireNonNull(fuelType, "fuelType must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.subsystems = new ArrayList<>(subsystems == null ? List.of() : subsystems);
        this.controllers = new ArrayList<>(controllers == null ? List.of() : controllers);
    }

    public HvofSystem(OrganizationId organizationId, HvofSystemCode code, String serialNumber,
                      String systemManufacturer, String systemModel, FuelType fuelType) {
        this(null, organizationId, code, serialNumber, systemManufacturer, systemModel,
                fuelType, HvofSystemStatus.ACTIVE, List.of(), List.of());
    }

    public HvofSystem(RegisterHvofSystemCommand command) {
        this(new OrganizationId(command.organizationId()), new HvofSystemCode(command.code()),
                command.serialNumber(), command.systemManufacturer(), command.systemModel(),
                FuelType.fromString(command.fuelType()));
    }

    public static HvofSystem reconstitute(Long id, OrganizationId organizationId, HvofSystemCode code,
                                          String serialNumber, String systemManufacturer, String systemModel,
                                          FuelType fuelType, HvofSystemStatus status,
                                          List<HvofSubsystem> subsystems, List<Controller> controllers) {
        return new HvofSystem(id, organizationId, code, serialNumber, systemManufacturer,
                systemModel, fuelType, status, subsystems, controllers);
    }

    public HvofSubsystem addSubsystem(SubsystemType type, String name, String alias, List<String> parameters) {
        if (subsystems.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name.trim()))) {
            throw new IllegalStateException("A subsystem named '" + name + "' already exists in this system");
        }
        var subsystem = new HvofSubsystem(type, name, alias, parameters);
        subsystems.add(subsystem);
        return subsystem;
    }

    public Controller addController(int controllerNumber, DeviceType deviceType, String serialNumber,
                                     String manufacturer, String model, String partNumber,
                                     NetworkEndpoint endpoint, String endpointUrl, String tagCatalogId,
                                     List<Protocol> protocols) {
        if (controllers.stream().anyMatch(c -> c.getControllerNumber() == controllerNumber)) {
            throw new IllegalStateException("Controller number " + controllerNumber + " already exists in this system");
        }
        var controller = new Controller(controllerNumber, deviceType, serialNumber, manufacturer,
                model, partNumber, endpoint, endpointUrl, tagCatalogId, protocols);
        controllers.add(controller);
        return controller;
    }

    public Long getOrganizationIdValue() {
        return organizationId.organizationId();
    }

    public String getCodeValue() {
        return code.code();
    }

    public void changeStatus(HvofSystemStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public void onCreated() {
        registerDomainEvent(HvofSystemRegisteredEvent.from(this));
    }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be null or blank");
        }
        return value.trim();
    }
}