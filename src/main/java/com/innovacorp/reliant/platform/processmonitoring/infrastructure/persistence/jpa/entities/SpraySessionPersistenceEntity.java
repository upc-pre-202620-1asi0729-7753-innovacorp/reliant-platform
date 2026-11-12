package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.SpraySessionStatus;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.converters.HvofSystemIdPersistenceConverter;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.converters.RecuperationIdPersistenceConverter;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.embeddables.RecipeBandPersistenceEmbeddable;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spray_sessions", indexes = {
        @Index(name = "ix_session_organization", columnList = "organization_id"),
        @Index(name = "ix_session_recuperation", columnList = "recuperation_id")
})
@Getter
@Setter
@NoArgsConstructor
public class SpraySessionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Convert(converter = HvofSystemIdPersistenceConverter.class)
    @Column(name = "hvof_system_id", nullable = false)
    private HvofSystemId hvofSystemId;

    @Convert(converter = RecuperationIdPersistenceConverter.class)
    @Column(name = "recuperation_id", nullable = false)
    private RecuperationId recuperationId;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "recipe_number", nullable = false)
    private Integer recipeNumber;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "time_zone", nullable = false, length = 40)
    private String timeZone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SpraySessionStatus status;

    @Column(name = "abort_reason", length = 200)
    private String abortReason;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "spray_session_recipe_bands", joinColumns = @JoinColumn(name = "spray_session_id"))
    private List<RecipeBandPersistenceEmbeddable> recipeBands = new ArrayList<>();

    @OneToMany(mappedBy = "spraySession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SprayPassPersistenceEntity> passes = new ArrayList<>();
}
