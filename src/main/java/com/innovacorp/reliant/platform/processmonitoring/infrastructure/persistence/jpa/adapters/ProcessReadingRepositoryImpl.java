package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.ProcessReadingRepository;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.assemblers.ProcessReadingPersistenceAssembler;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.repositories.ProcessReadingPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProcessReadingRepositoryImpl implements ProcessReadingRepository {

    private final ProcessReadingPersistenceRepository persistenceRepository;

    public ProcessReadingRepositoryImpl(ProcessReadingPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<ProcessReading> saveAll(List<ProcessReading> readings) {
        var entities = readings.stream()
                .map(ProcessReadingPersistenceAssembler::toPersistenceFromDomain)
                .toList();

        return persistenceRepository.saveAll(entities).stream()
                .map(ProcessReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<ProcessReading> findAllBySpraySessionId(Long spraySessionId) {
        return persistenceRepository.findAllBySpraySessionIdOrderByEpochMillisAsc(spraySessionId)
                .stream()
                .map(ProcessReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<ProcessReading> findAllBySpraySessionIdAndBand(Long spraySessionId, Band band) {
        return persistenceRepository.findAllBySpraySessionIdAndBandOrderByEpochMillisAsc(spraySessionId, band)
                .stream()
                .map(ProcessReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public long countBySpraySessionIdAndBand(Long spraySessionId, Band band) {
        return persistenceRepository.countBySpraySessionIdAndBand(spraySessionId, band);
    }
}