package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.FuelType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemStatus;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.converters.HvofSystemCodePersistenceConverter;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for HVOF systems.
 */
@Entity
@Table(name = "hvof_systems", uniqueConstraints = @UniqueConstraint(name = "uk_hvof_system_organization_code", columnNames = {"organization_id", "code"}))
@Getter
@Setter
@NoArgsConstructor
public class HvofSystemPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Convert(converter = HvofSystemCodePersistenceConverter.class)
    @Column(name = "code", nullable = false, length = 20)
    private HvofSystemCode code;

    @Column(name = "serial_number", nullable = false, length = 60)
    private String serialNumber;

    @Column(name = "system_manufacturer", nullable = false, length = 80)
    private String systemManufacturer;

    @Column(name = "system_model", nullable = false, length = 80)
    private String systemModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 20)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HvofSystemStatus status;
}