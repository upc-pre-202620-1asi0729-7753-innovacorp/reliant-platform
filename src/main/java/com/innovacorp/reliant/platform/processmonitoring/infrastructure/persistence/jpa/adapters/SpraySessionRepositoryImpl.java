package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.SpraySessionRepository;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.assemblers.SpraySessionPersistenceAssembler;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.repositories.SpraySessionPersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpraySessionRepositoryImpl implements SpraySessionRepository {

    private final SpraySessionPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public SpraySessionRepositoryImpl(SpraySessionPersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SpraySession save(SpraySession session) {
        boolean isNew = session.getId() == null;
        var savedEntity = persistenceRepository.save(SpraySessionPersistenceAssembler.toPersistenceFromDomain(session));
        var saved = SpraySessionPersistenceAssembler.toDomainFromPersistence(savedEntity);
        if (isNew) {
            saved.onCreated();
        }
        domainEventPublisher.publishAndClearEvents(session);
        domainEventPublisher.publishAndClearEvents(saved);
        return saved;
    }

    @Override
    public Optional<SpraySession> findById(Long id) {
        return persistenceRepository.findById(id).map(SpraySessionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<SpraySession> findAllByOrganizationId(OrganizationId organizationId) {
        return persistenceRepository.findAllByOrganizationId(organizationId).stream().map(SpraySessionPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<SpraySession> findAllByRecuperationId(RecuperationId recuperationId) {
        return persistenceRepository.findAllByRecuperationId(recuperationId).stream().map(SpraySessionPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
