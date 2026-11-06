package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.HvofSystemPersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for HVOF system persistence entities.
 * Converter-mapped fields are queried with explicit JPQL.
 */
@Repository
public interface HvofSystemPersistenceRepository extends JpaRepository<HvofSystemPersistenceEntity, Long> {

    @Query("select count(s) from HvofSystemPersistenceEntity s where s.organizationId = :organizationId and s.code = :code")
    long countByOrganizationIdAndCode(@Param("organizationId") OrganizationId organizationId, @Param("code") HvofSystemCode code);

    @Query("select s from HvofSystemPersistenceEntity s where s.organizationId = :organizationId order by s.code")
    List<HvofSystemPersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId);
}