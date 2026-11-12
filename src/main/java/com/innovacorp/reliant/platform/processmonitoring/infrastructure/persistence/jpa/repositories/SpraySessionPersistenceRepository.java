package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities.SpraySessionPersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpraySessionPersistenceRepository extends JpaRepository<SpraySessionPersistenceEntity, Long> {

    @Query("select s from SpraySessionPersistenceEntity s where s.organizationId = :organizationId order by s.startedAt desc")
    List<SpraySessionPersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId);

    @Query("select s from SpraySessionPersistenceEntity s where s.recuperationId = :recuperationId order by s.startedAt desc")
    List<SpraySessionPersistenceEntity> findAllByRecuperationId(@Param("recuperationId") RecuperationId recuperationId);
}
