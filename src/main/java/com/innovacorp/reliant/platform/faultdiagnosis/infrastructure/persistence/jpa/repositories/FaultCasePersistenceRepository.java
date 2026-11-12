package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultCaseStatus;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.entities.FaultCasePersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaultCasePersistenceRepository extends JpaRepository<FaultCasePersistenceEntity, Long> {

    @Query("""
            select c from FaultCasePersistenceEntity c
            where c.organizationId = :organizationId
              and (:spraySessionId is null or c.spraySessionId = :spraySessionId)
              and (:status is null or c.status = :status)
            order by c.epochMillis desc""")
    List<FaultCasePersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId,
                                                             @Param("spraySessionId") SpraySessionId spraySessionId,
                                                             @Param("status") FaultCaseStatus status);

    @Query("select c from FaultCasePersistenceEntity c where c.spraySessionId = :spraySessionId and c.parameter = :parameter and c.band = :band order by c.epochMillis desc limit 1")
    Optional<FaultCasePersistenceEntity> findLatestBySpraySessionIdAndParameterAndBand(@Param("spraySessionId") SpraySessionId spraySessionId,
                                                                                        @Param("parameter") String parameter, @Param("band") FaultBand band);
}
