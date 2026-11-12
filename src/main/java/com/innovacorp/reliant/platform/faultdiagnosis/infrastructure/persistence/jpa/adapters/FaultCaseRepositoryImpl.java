package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultCaseStatus;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultCaseRepository;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.assemblers.FaultCasePersistenceAssembler;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.repositories.FaultCasePersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FaultCaseRepositoryImpl implements FaultCaseRepository {

    private final FaultCasePersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public FaultCaseRepositoryImpl(FaultCasePersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public FaultCase save(FaultCase faultCase) {
        var isNew = faultCase.getId() == null;
        var saved = FaultCasePersistenceAssembler.toDomainFromPersistence(persistenceRepository.save(FaultCasePersistenceAssembler.toPersistenceFromDomain(faultCase)));
        if (isNew) {
            saved.onOpened();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public Optional<FaultCase> findById(Long id) {
        return persistenceRepository.findById(id).map(FaultCasePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<FaultCase> findAllByOrganizationId(OrganizationId organizationId, SpraySessionId spraySessionId, FaultCaseStatus status) {
        return persistenceRepository.findAllByOrganizationId(organizationId, spraySessionId, status).stream().map(FaultCasePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<FaultCase> findLatestBySpraySessionIdAndParameterAndBand(SpraySessionId spraySessionId, String parameter, FaultBand band) {
        return persistenceRepository.findLatestBySpraySessionIdAndParameterAndBand(spraySessionId, parameter, band).map(FaultCasePersistenceAssembler::toDomainFromPersistence);
    }
}
