package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "spray_passes", uniqueConstraints = @UniqueConstraint(name = "uk_pass_session_number", columnNames = {"spray_session_id", "pass_number"}))
@Getter
@Setter
@NoArgsConstructor
public class SprayPassPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spray_session_id", nullable = false)
    private SpraySessionPersistenceEntity spraySession;

    @Column(name = "pass_number", nullable = false)
    private int passNumber;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;
}
