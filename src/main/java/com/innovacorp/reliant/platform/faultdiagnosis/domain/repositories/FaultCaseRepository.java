package com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultCaseStatus;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

public interface FaultCaseRepository {
    FaultCase save(FaultCase faultCase);

    Optional<FaultCase> findById(Long id);

    List<FaultCase> findAllByOrganizationId(OrganizationId organizationId, SpraySessionId spraySessionId, FaultCaseStatus status);

    /** Latest case of the same session/parameter/band, used for the cooldown. */
    Optional<FaultCase> findLatestBySpraySessionIdAndParameterAndBand(SpraySessionId spraySessionId, String parameter, FaultBand band);
}
