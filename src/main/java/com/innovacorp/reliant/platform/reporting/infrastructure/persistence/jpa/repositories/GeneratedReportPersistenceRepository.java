package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities.GeneratedReportPersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratedReportPersistenceRepository extends JpaRepository<GeneratedReportPersistenceEntity, Long> {

    @Query("""
            select r from GeneratedReportPersistenceEntity r
            where r.organizationId = :organizationId
              and (:subjectType is null or r.subjectType = :subjectType)
              and (:subjectId is null or r.subjectId = :subjectId)
            order by r.generatedAt desc""")
    List<GeneratedReportPersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId,
                                                                   @Param("subjectType") ReportSubjectType subjectType,
                                                                   @Param("subjectId") Long subjectId);
}
