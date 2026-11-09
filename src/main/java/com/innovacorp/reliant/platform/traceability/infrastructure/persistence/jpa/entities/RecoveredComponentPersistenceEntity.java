package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentStatus;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentType;
import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters.CustomerIdPersistenceConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "components", uniqueConstraints = @UniqueConstraint(
        name = "uk_component_customer_serial", columnNames = {"customer_id", "serial_number"}))
@Getter
@Setter
@NoArgsConstructor
public class RecoveredComponentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = CustomerIdPersistenceConverter.class)
    @Column(name = "customer_id", nullable = false)
    private CustomerId customerId;

    @Column(name = "serial_number", nullable = false, length = 60)
    private String serialNumber;

    @Column(name = "part_number", nullable = false, length = 40)
    private String partNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false, length = 30)
    private ComponentType componentType;

    @Column(name = "machine_manufacturer", nullable = false, length = 80)
    private String machineManufacturer;

    @Column(name = "machine_model", nullable = false, length = 80)
    private String machineModel;

    @Column(name = "pcr_target_hours", nullable = false)
    private Integer pcrTargetHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ComponentStatus status;
}