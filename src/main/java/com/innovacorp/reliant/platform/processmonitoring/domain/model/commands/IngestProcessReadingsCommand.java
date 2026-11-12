package com.innovacorp.reliant.platform.processmonitoring.domain.model.commands;

import java.util.List;

public record IngestProcessReadingsCommand(Long spraySessionId, List<ReadingItem> readings) {

    public record ReadingItem(
            long epochMillis,
            Long plcClockOffsetMillis,
            String tagPath,
            String parameter,
            Long subsystemId,
            Long partId,
            double value
    ) {
    }
}