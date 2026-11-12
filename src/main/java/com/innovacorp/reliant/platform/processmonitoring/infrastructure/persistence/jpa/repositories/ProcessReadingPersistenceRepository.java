package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities.ProcessReadingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessReadingPersistenceRepository extends JpaRepository<ProcessReadingPersistenceEntity, Long> {

    List<ProcessReadingPersistenceEntity> findAllBySpraySessionIdOrderByEpochMillisAsc(Long spraySessionId);

    List<ProcessReadingPersistenceEntity> findAllBySpraySessionIdAndBandOrderByEpochMillisAsc(
            Long spraySessionId, Band band);

    long countBySpraySessionIdAndBand(Long spraySessionId, Band band);
}