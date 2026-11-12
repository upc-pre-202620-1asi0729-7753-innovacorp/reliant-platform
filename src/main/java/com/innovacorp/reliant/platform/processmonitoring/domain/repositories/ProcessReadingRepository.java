package com.innovacorp.reliant.platform.processmonitoring.domain.repositories;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;

import java.util.List;

public interface ProcessReadingRepository {
    List<ProcessReading> saveAll(List<ProcessReading> readings);

    List<ProcessReading> findAllBySpraySessionId(Long spraySessionId);

    List<ProcessReading> findAllBySpraySessionIdAndBand(Long spraySessionId, Band band);

    long countBySpraySessionIdAndBand(Long spraySessionId, Band band);
}
