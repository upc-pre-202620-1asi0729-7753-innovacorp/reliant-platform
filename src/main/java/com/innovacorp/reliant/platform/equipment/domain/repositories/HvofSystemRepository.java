package com.innovacorp.reliant.platform.equipment.domain.repositories;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

/**
 * HVOF System repository port.
 */
public interface HvofSystemRepository {
    HvofSystem save(HvofSystem hvofSystem);

    boolean existsByOrganizationIdAndCode(OrganizationId organizationId, HvofSystemCode code);

    Optional<HvofSystem> findById(Long id);

    List<HvofSystem> findAllByOrganizationId(OrganizationId organizationId);
}