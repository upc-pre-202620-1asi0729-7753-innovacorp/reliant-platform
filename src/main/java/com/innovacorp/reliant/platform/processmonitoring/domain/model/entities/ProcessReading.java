package com.innovacorp.reliant.platform.processmonitoring.domain.model.entities;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import lombok.Getter;

/**
 * One classified process reading. Stored in its own table (high volume), referenced to the session by id.
 */
@Getter
public class ProcessReading {
    private Long id;
    private final Long spraySessionId;
    private final long epochMillis;
    private final long plcClockOffsetMillis;
    private final String tagPath;
    private final String parameter;
    private final Long subsystemId;
    private final Long partId;
    private final double value;
    private final String unitSymbol;
    private final String unitCategory;
    private final Band band;
    private final boolean derived;
    private final boolean mappingPending;

    public ProcessReading(Long spraySessionId, long epochMillis, long plcClockOffsetMillis, String tagPath, String parameter, Long subsystemId, Long partId,
                          double value, String unitSymbol, String unitCategory, Band band, boolean derived, boolean mappingPending) {
        if (spraySessionId == null || spraySessionId <= 0) throw new IllegalArgumentException("Spray session id must be positive");
        if (epochMillis <= 0) throw new IllegalArgumentException("epochMillis must be positive");
        if (tagPath == null || tagPath.isBlank()) throw new IllegalArgumentException("tagPath must not be blank");
        if (parameter == null || parameter.isBlank()) throw new IllegalArgumentException("parameter must not be blank");
        this.spraySessionId = spraySessionId;
        this.epochMillis = epochMillis;
        this.plcClockOffsetMillis = plcClockOffsetMillis;
        this.tagPath = tagPath.trim();
        this.parameter = parameter.trim();
        this.subsystemId = subsystemId;
        this.partId = partId;
        this.value = value;
        this.unitSymbol = unitSymbol == null ? "" : unitSymbol;
        this.unitCategory = unitCategory == null ? "" : unitCategory;
        this.band = band == null ? Band.NOMINAL : band;
        this.derived = derived;
        this.mappingPending = mappingPending;
    }

    public static ProcessReading reconstitute(Long id, Long spraySessionId, long epochMillis, long plcClockOffsetMillis, String tagPath, String parameter,
                                              Long subsystemId, Long partId, double value, String unitSymbol, String unitCategory, Band band, boolean derived, boolean mappingPending) {
        var reading = new ProcessReading(spraySessionId, epochMillis, plcClockOffsetMillis, tagPath, parameter, subsystemId, partId, value, unitSymbol, unitCategory, band, derived, mappingPending);
        reading.id = id;
        return reading;
    }
}
