package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.IngestProcessReadingsCommand;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.IngestProcessReadingResource;

import java.util.List;

public class IngestProcessReadingsCommandFromResourceAssembler {

    public static IngestProcessReadingsCommand toCommandFromResources(
            Long spraySessionId, List<IngestProcessReadingResource> resources) {
        var items = resources.stream()
                .map(resource -> new IngestProcessReadingsCommand.ReadingItem(
                        resource.epochMillis(), resource.plcClockOffsetMillis(),
                        resource.tagPath(), resource.parameter(), resource.subsystemId(),
                        resource.partId(), resource.value()))
                .toList();

        return new IngestProcessReadingsCommand(spraySessionId, items);
    }
}