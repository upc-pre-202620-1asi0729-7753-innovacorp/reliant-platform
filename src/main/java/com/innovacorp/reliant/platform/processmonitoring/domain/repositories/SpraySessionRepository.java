package com.innovacorp.reliant.platform.processmonitoring.domain.repositories;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

public interface SpraySessionRepository {

    SpraySession save(SpraySession session);

    Optional<SpraySession> findById(Long id);

    List<SpraySession> findAllByOrganizationId(OrganizationId organizationId);

    List<SpraySession> findAllByRecuperationId(RecuperationId recuperationId);
}