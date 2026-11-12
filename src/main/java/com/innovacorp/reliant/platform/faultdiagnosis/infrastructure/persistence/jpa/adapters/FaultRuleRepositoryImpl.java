package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultRuleRepository;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.assemblers.FaultRulePersistenceAssembler;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.repositories.FaultRulePersistenceRepository;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FaultRuleRepositoryImpl implements FaultRuleRepository {

    private final FaultRulePersistenceRepository persistenceRepository;

    public FaultRuleRepositoryImpl(FaultRulePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public FaultRule save(FaultRule rule) {
        return FaultRulePersistenceAssembler.toDomainFromPersistence(persistenceRepository.save(FaultRulePersistenceAssembler.toPersistenceFromDomain(rule)));
    }

    @Override
    public Optional<FaultRule> findById(Long id) {
        return persistenceRepository.findById(id).map(FaultRulePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<FaultRule> findAllByOrganizationId(OrganizationId organizationId) {
        return persistenceRepository.findAllByOrganizationId(organizationId).stream().map(FaultRulePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<FaultRule> findByOrganizationIdAndParameterAndBand(OrganizationId organizationId, String parameter, FaultBand band) {
        return persistenceRepository.findByOrganizationIdAndParameterAndBand(organizationId, parameter, band).map(FaultRulePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByOrganizationId(OrganizationId organizationId) {
        return persistenceRepository.countByOrganizationId(organizationId) > 0;
    }
}
