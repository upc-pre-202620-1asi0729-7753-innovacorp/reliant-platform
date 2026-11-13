package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.reporting.domain.repositories.GeneratedReportRepository;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.assemblers.GeneratedReportPersistenceAssembler;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.repositories.GeneratedReportPersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GeneratedReportRepositoryImpl implements GeneratedReportRepository {

    private final GeneratedReportPersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public GeneratedReportRepositoryImpl(GeneratedReportPersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public GeneratedReport save(GeneratedReport report) {
        var isNew = report.getId() == null;
        var saved = GeneratedReportPersistenceAssembler.toDomainFromPersistence(persistenceRepository.save(GeneratedReportPersistenceAssembler.toPersistenceFromDomain(report)));
        if (isNew) {
            saved.onRegistered();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public Optional<GeneratedReport> findById(Long id) {
        return persistenceRepository.findById(id).map(GeneratedReportPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<GeneratedReport> findAllByOrganizationId(OrganizationId organizationId, ReportSubjectType subjectType, Long subjectId) {
        return persistenceRepository.findAllByOrganizationId(organizationId, subjectType, subjectId).stream().map(GeneratedReportPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
