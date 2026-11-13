package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.reporting.domain.repositories.ReportTemplateRepository;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.assemblers.ReportTemplatePersistenceAssembler;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.repositories.ReportTemplatePersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReportTemplateRepositoryImpl implements ReportTemplateRepository {

    private final ReportTemplatePersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public ReportTemplateRepositoryImpl(ReportTemplatePersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public ReportTemplate save(ReportTemplate template) {
        var isNew = template.getId() == null;
        var saved = ReportTemplatePersistenceAssembler.toDomainFromPersistence(persistenceRepository.save(ReportTemplatePersistenceAssembler.toPersistenceFromDomain(template)));
        if (isNew) {
            saved.onCreated();
            domainEventPublisher.publishAndClearEvents(saved);
        }
        return saved;
    }

    @Override
    public Optional<ReportTemplate> findById(Long id) {
        return persistenceRepository.findById(id).map(ReportTemplatePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<ReportTemplate> findAllByOrganizationId(OrganizationId organizationId, ReportKind reportKind) {
        return persistenceRepository.findAllByOrganizationId(organizationId, reportKind).stream().map(ReportTemplatePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<ReportTemplate> findDefaultsByOrganizationIdAndKind(OrganizationId organizationId, ReportKind reportKind) {
        return persistenceRepository.findDefaultsByOrganizationIdAndKind(organizationId, reportKind).stream().map(ReportTemplatePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public boolean existsByOrganizationId(OrganizationId organizationId) {
        return persistenceRepository.countByOrganizationId(organizationId) > 0;
    }

    @Override
    public boolean existsByOrganizationIdAndName(OrganizationId organizationId, String name, Long excludingId) {
        return persistenceRepository.countByOrganizationIdAndName(organizationId, name, excludingId) > 0;
    }
}
