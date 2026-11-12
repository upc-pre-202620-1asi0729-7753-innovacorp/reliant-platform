package com.innovacorp.reliant.platform.processmonitoring.domain.model.entities;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SprayPass {

    private Long id;
    private final int passNumber;
    private final Instant startedAt;
    private final Instant endedAt;

    public SprayPass(int passNumber, Instant startedAt, Instant endedAt) {
        if (passNumber <= 0) {
            throw new IllegalArgumentException("Pass number must be positive");
        }
        if (startedAt == null) {
            throw new IllegalArgumentException("Pass start time is required");
        }
        if (endedAt != null && endedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Pass end time cannot be before its start time");
        }

        this.passNumber = passNumber;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static SprayPass reconstitute(Long id, int passNumber, Instant startedAt, Instant endedAt) {
        var pass = new SprayPass(passNumber, startedAt, endedAt);
        pass.id = id;
        return pass;
    }
}