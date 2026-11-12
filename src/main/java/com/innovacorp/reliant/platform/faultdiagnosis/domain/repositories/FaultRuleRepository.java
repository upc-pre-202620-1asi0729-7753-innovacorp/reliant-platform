package com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

public interface FaultRuleRepository {
    FaultRule save(FaultRule rule);

    Optional<FaultRule> findById(Long id);

    List<FaultRule> findAllByOrganizationId(OrganizationId organizationId);

    Optional<FaultRule> findByOrganizationIdAndParameterAndBand(OrganizationId organizationId, String parameter, FaultBand band);

    boolean existsByOrganizationId(OrganizationId organizationId);
}
