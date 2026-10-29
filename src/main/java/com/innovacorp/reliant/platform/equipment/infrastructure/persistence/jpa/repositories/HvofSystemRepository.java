package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HvofSystemRepository extends JpaRepository<HvofSystem, Long> {
    boolean existsByOrganizationIdAndCode(OrganizationId organizationId, HvofSystemCode code);
    List<HvofSystem> findAllByOrganizationId(OrganizationId organizationId);
}