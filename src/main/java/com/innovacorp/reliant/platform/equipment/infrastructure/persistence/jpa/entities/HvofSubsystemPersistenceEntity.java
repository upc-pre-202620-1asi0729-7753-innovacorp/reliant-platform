package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.SubsystemType;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hvof_subsystems", uniqueConstraints = @UniqueConstraint(name = "uk_subsystem_system_name", columnNames = {"hvof_system_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
public class HvofSubsystemPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hvof_system_id", nullable = false)
    private HvofSystemPersistenceEntity hvofSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "subsystem_type", nullable = false, length = 30)
    private SubsystemType subsystemType;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "alias", nullable = false, length = 40)
    private String alias;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hvof_subsystem_parameters", joinColumns = @JoinColumn(name = "hvof_subsystem_id"))
    @Column(name = "parameter", nullable = false, length = 40)
    private List<String> parameters = new ArrayList<>();
}
