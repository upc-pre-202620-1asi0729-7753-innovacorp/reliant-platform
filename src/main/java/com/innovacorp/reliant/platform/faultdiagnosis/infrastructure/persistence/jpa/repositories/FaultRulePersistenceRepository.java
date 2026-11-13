package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.entities.FaultRulePersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaultRulePersistenceRepository extends JpaRepository<FaultRulePersistenceEntity, Long> {

    @Query("select r from FaultRulePersistenceEntity r where r.organizationId = :organizationId order by r.parameter, r.band")
    List<FaultRulePersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId);

    @Query("select r from FaultRulePersistenceEntity r where r.organizationId = :organizationId and r.parameter = :parameter and r.band = :band")
    Optional<FaultRulePersistenceEntity> findByOrganizationIdAndParameterAndBand(@Param("organizationId") OrganizationId organizationId,
                                                                                 @Param("parameter") String parameter, @Param("band") FaultBand band);

    @Query("select count(r) from FaultRulePersistenceEntity r where r.organizationId = :organizationId")
    long countByOrganizationId(@Param("organizationId") OrganizationId organizationId);
}
