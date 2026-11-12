package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "process_readings", indexes = {
        @Index(name = "ix_reading_session_time", columnList = "spray_session_id, epoch_millis"),
        @Index(name = "ix_reading_session_band", columnList = "spray_session_id, band"),
        @Index(name = "ix_reading_session_parameter", columnList = "spray_session_id, parameter")
})
@Getter
@Setter
@NoArgsConstructor
public class ProcessReadingPersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spray_session_id", nullable = false)
    private Long spraySessionId;

    @Column(name = "epoch_millis", nullable = false)
    private long epochMillis;

    @Column(name = "plc_clock_offset_millis", nullable = false)
    private long plcClockOffsetMillis;

    @Column(name = "tag_path", nullable = false, length = 80)
    private String tagPath;

    @Column(name = "parameter", nullable = false, length = 40)
    private String parameter;

    @Column(name = "subsystem_id")
    private Long subsystemId;

    @Column(name = "part_id")
    private Long partId;

    @Column(name = "reading_value", nullable = false)
    private double value;

    @Column(name = "unit_symbol", nullable = false, length = 10)
    private String unitSymbol;

    @Column(name = "unit_category", nullable = false, length = 20)
    private String unitCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "band", nullable = false, length = 20)
    private Band band;

    @Column(name = "derived", nullable = false)
    private boolean derived;

    @Column(name = "mapping_pending", nullable = false)
    private boolean mappingPending;
}
