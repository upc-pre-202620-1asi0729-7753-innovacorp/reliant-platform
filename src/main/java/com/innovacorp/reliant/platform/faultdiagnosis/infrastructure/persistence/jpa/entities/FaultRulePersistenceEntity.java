package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fault_rules", uniqueConstraints = @UniqueConstraint(name = "uk_rule_org_parameter_band", columnNames = {"organization_id", "parameter", "band"}))
@Getter
@Setter
@NoArgsConstructor
public class FaultRulePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Column(name = "parameter", nullable = false, length = 40)
    private String parameter;

    @Enumerated(EnumType.STRING)
    @Column(name = "band", nullable = false, length = 20)
    private FaultBand band;

    @Column(name = "probable_cause", nullable = false, length = 300)
    private String probableCause;

    @Column(name = "recommended_action", nullable = false, length = 300)
    private String recommendedAction;

    @Column(name = "cooldown_seconds", nullable = false)
    private int cooldownSeconds;
}
