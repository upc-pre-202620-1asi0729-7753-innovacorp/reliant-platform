package com.innovacorp.reliant.platform.processmonitoring.application.internal.queryservices;

import com.innovacorp.reliant.platform.processmonitoring.application.queryservices.SpraySessionQueryService;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.queries.*;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.ProcessReadingRepository;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.SpraySessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SpraySessionQueryServiceImpl implements SpraySessionQueryService {
    private final SpraySessionRepository sessionRepository;
    private final ProcessReadingRepository readingRepository;

    public SpraySessionQueryServiceImpl(SpraySessionRepository sessionRepository, ProcessReadingRepository readingRepository) {
        this.sessionRepository = sessionRepository;
        this.readingRepository = readingRepository;
    }

    @Override
    public Optional<SpraySession> handle(GetSpraySessionByIdQuery query) {
        return sessionRepository.findById(query.spraySessionId());
    }

    @Override
    public List<SpraySession> handle(GetAllSpraySessionsByOrganizationIdQuery query) {
        return sessionRepository.findAllByOrganizationId(query.organizationId());
    }

    @Override
    public List<SpraySession> handle(GetAllSpraySessionsByRecuperationIdQuery query) {
        return sessionRepository.findAllByRecuperationId(query.recuperationId());
    }

    @Override
    public List<ProcessReading> handle(GetReadingsBySpraySessionIdQuery query) {
        return query.band() == null
                ? readingRepository.findAllBySpraySessionId(query.spraySessionId())
                : readingRepository.findAllBySpraySessionIdAndBand(query.spraySessionId(), query.band());
    }

    @Override
    public Map<String, Long> handle(GetDeviationCountsBySpraySessionIdQuery query) {
        return Map.of(
                "warning", readingRepository.countBySpraySessionIdAndBand(query.spraySessionId(), Band.WARNING),
                "shutdown", readingRepository.countBySpraySessionIdAndBand(query.spraySessionId(), Band.SHUTDOWN));
    }
}
