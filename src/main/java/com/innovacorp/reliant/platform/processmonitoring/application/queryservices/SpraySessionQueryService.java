package com.innovacorp.reliant.platform.processmonitoring.application.queryservices;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.queries.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SpraySessionQueryService {

    Optional<SpraySession> handle(GetSpraySessionByIdQuery query);

    List<SpraySession> handle(GetAllSpraySessionsByOrganizationIdQuery query);

    List<SpraySession> handle(GetAllSpraySessionsByRecuperationIdQuery query);

    List<ProcessReading> handle(GetReadingsBySpraySessionIdQuery query);

    Map<String, Long> handle(GetDeviationCountsBySpraySessionIdQuery query);
}