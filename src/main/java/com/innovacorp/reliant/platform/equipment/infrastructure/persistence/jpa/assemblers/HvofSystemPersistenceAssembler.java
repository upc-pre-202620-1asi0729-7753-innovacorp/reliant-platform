package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.NetworkEndpoint;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables.NetworkEndpointPersistenceEmbeddable;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.ControllerPersistenceEntity;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.HvofSubsystemPersistenceEntity;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.HvofSystemPersistenceEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Static assembler between HVOF system domain and persistence representations (aggregate + children).
 */
public final class HvofSystemPersistenceAssembler {

    private HvofSystemPersistenceAssembler() {
    }

    public static HvofSystem toDomainFromPersistence(HvofSystemPersistenceEntity entity) {
        if (entity == null) return null;
        return HvofSystem.reconstitute(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getCode(),
                entity.getSerialNumber(),
                entity.getSystemManufacturer(),
                entity.getSystemModel(),
                entity.getFuelType(),
                entity.getStatus(),
                entity.getSubsystems().stream().map(HvofSystemPersistenceAssembler::toDomainFromPersistence).toList(),
                entity.getControllers().stream().map(HvofSystemPersistenceAssembler::toDomainFromPersistence).toList());
    }

    public static HvofSystemPersistenceEntity toPersistenceFromDomain(HvofSystem hvofSystem) {
        if (hvofSystem == null) return null;
        var entity = new HvofSystemPersistenceEntity();
        if (hvofSystem.getId() != null) {
            entity.setId(hvofSystem.getId());
        }
        entity.setOrganizationId(hvofSystem.getOrganizationId());
        entity.setCode(hvofSystem.getCode());
        entity.setSerialNumber(hvofSystem.getSerialNumber());
        entity.setSystemManufacturer(hvofSystem.getSystemManufacturer());
        entity.setSystemModel(hvofSystem.getSystemModel());
        entity.setFuelType(hvofSystem.getFuelType());
        entity.setStatus(hvofSystem.getStatus());
        entity.setSubsystems(hvofSystem.getSubsystems().stream()
                .map(s -> toPersistenceFromDomain(s, entity))
                .collect(Collectors.toCollection(ArrayList::new)));
        entity.setControllers(hvofSystem.getControllers().stream()
                .map(c -> toPersistenceFromDomain(c, entity))
                .collect(Collectors.toCollection(ArrayList::new)));
        return entity;
    }

    private static HvofSubsystem toDomainFromPersistence(HvofSubsystemPersistenceEntity e) {
        return HvofSubsystem.reconstitute(e.getId(), e.getSubsystemType(), e.getName(), e.getAlias(), new ArrayList<>(e.getParameters()));
    }

    private static HvofSubsystemPersistenceEntity toPersistenceFromDomain(HvofSubsystem s, HvofSystemPersistenceEntity system) {
        var e = new HvofSubsystemPersistenceEntity();
        if (s.getId() != null) {
            e.setId(s.getId());
        }
        e.setHvofSystem(system);
        e.setSubsystemType(s.getSubsystemType());
        e.setName(s.getName());
        e.setAlias(s.getAlias());
        e.setParameters(new ArrayList<>(s.getParameters()));
        return e;
    }

    private static Controller toDomainFromPersistence(ControllerPersistenceEntity e) {
        var ep = e.getEndpoint();
        return Controller.reconstitute(e.getId(), e.getControllerNumber(), e.getDeviceType(), e.getSerialNumber(), e.getManufacturer(), e.getModel(),
                e.getPartNumber(), new NetworkEndpoint(ep.getIpAddress(), ep.getPort(), ep.getRack(), ep.getSlot()),
                e.getEndpointUrl(), e.getTagCatalogId(), new ArrayList<>(e.getSupportedProtocols()));
    }

    private static ControllerPersistenceEntity toPersistenceFromDomain(Controller c, HvofSystemPersistenceEntity system) {
        var e = new ControllerPersistenceEntity();
        if (c.getId() != null) {
            e.setId(c.getId());
        }
        e.setHvofSystem(system);
        e.setControllerNumber(c.getControllerNumber());
        e.setDeviceType(c.getDeviceType());
        e.setSerialNumber(c.getSerialNumber());
        e.setManufacturer(c.getManufacturer());
        e.setModel(c.getModel());
        e.setPartNumber(c.getPartNumber());
        e.setEndpoint(new NetworkEndpointPersistenceEmbeddable(c.getEndpoint().ipAddress(), c.getEndpoint().port(), c.getEndpoint().rack(), c.getEndpoint().slot()));
        e.setEndpointUrl(c.getEndpointUrl());
        e.setTagCatalogId(c.getTagCatalogId());
        e.setSupportedProtocols(new ArrayList<>(c.getSupportedProtocols()));
        return e;
    }
}
