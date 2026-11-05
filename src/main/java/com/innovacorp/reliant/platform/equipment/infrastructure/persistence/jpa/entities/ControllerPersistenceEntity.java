package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.DeviceType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.Protocol;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables.NetworkEndpointPersistenceEmbeddable;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "controllers", uniqueConstraints = @UniqueConstraint(name = "uk_controller_system_number", columnNames = {"hvof_system_id", "controller_number"}))
@Getter
@Setter
@NoArgsConstructor
public class ControllerPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hvof_system_id", nullable = false)
    private HvofSystemPersistenceEntity hvofSystem;

    @Column(name = "controller_number", nullable = false)
    private int controllerNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 20)
    private DeviceType deviceType;

    @Column(name = "serial_number", nullable = false, length = 60)
    private String serialNumber;

    @Column(name = "manufacturer", nullable = false, length = 80)
    private String manufacturer;

    @Column(name = "model", nullable = false, length = 80)
    private String model;

    @Column(name = "part_number", length = 40)
    private String partNumber;

    @Embedded
    private NetworkEndpointPersistenceEmbeddable endpoint;

    @Column(name = "endpoint_url", length = 200)
    private String endpointUrl;

    @Column(name = "tag_catalog_id", length = 60)
    private String tagCatalogId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "controller_supported_protocols", joinColumns = @JoinColumn(name = "controller_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "protocol", nullable = false, length = 20)
    private List<Protocol> supportedProtocols = new ArrayList<>();
}
