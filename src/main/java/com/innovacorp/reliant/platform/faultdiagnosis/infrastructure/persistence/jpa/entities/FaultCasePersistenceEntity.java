package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.*;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.converters.HvofSystemIdPersistenceConverter;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.converters.SpraySessionIdPersistenceConverter;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.converters.UserIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "fault_cases", indexes = {
        @Index(name = "ix_fault_case_org_status", columnList = "organization_id, status"),
        @Index(name = "ix_fault_case_session_parameter_band", columnList = "spray_session_id, parameter, band, epoch_millis")})
@Getter
@Setter
@NoArgsConstructor
public class FaultCasePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Convert(converter = SpraySessionIdPersistenceConverter.class)
    @Column(name = "spray_session_id", nullable = false)
    private SpraySessionId spraySessionId;

    @Convert(converter = HvofSystemIdPersistenceConverter.class)
    @Column(name = "hvof_system_id", nullable = false)
    private HvofSystemId hvofSystemId;

    @Column(name = "parameter", nullable = false, length = 40)
    private String parameter;

    @Enumerated(EnumType.STRING)
    @Column(name = "band", nullable = false, length = 20)
    private FaultBand band;

    @Column(name = "value", nullable = false)
    private double value;

    @Column(name = "epoch_millis", nullable = false)
    private long epochMillis;

    @Column(name = "probable_cause", nullable = false, length = 300)
    private String probableCause;

    @Column(name = "recommended_action", nullable = false, length = 300)
    private String recommendedAction;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FaultCaseStatus status;

    @Convert(converter = UserIdPersistenceConverter.class)
    @Column(name = "acknowledged_by")
    private UserId acknowledgedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "acknowledged_at")
    private Date acknowledgedAt;

    @Convert(converter = UserIdPersistenceConverter.class)
    @Column(name = "resolved_by")
    private UserId resolvedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "resolved_at")
    private Date resolvedAt;

    @Column(name = "resolution_notes", length = 500)
    private String resolutionNotes;
}
