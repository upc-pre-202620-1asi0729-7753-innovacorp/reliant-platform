package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities.ReportTemplatePersistenceEntity;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTemplatePersistenceRepository extends JpaRepository<ReportTemplatePersistenceEntity, Long> {

    @Query("""
            select t from ReportTemplatePersistenceEntity t
            where t.organizationId = :organizationId and (:reportKind is null or t.reportKind = :reportKind)
            order by t.reportKind, t.defaultForKind desc, t.name""")
    List<ReportTemplatePersistenceEntity> findAllByOrganizationId(@Param("organizationId") OrganizationId organizationId,
                                                                  @Param("reportKind") ReportKind reportKind);

    @Query("select t from ReportTemplatePersistenceEntity t where t.organizationId = :organizationId and t.reportKind = :reportKind and t.defaultForKind = true")
    List<ReportTemplatePersistenceEntity> findDefaultsByOrganizationIdAndKind(@Param("organizationId") OrganizationId organizationId,
                                                                              @Param("reportKind") ReportKind reportKind);

    @Query("select count(t) from ReportTemplatePersistenceEntity t where t.organizationId = :organizationId")
    long countByOrganizationId(@Param("organizationId") OrganizationId organizationId);

    @Query("select count(t) from ReportTemplatePersistenceEntity t where t.organizationId = :organizationId and t.name = :name and (:excludingId is null or t.id <> :excludingId)")
    long countByOrganizationIdAndName(@Param("organizationId") OrganizationId organizationId, @Param("name") String name, @Param("excludingId") Long excludingId);
}
