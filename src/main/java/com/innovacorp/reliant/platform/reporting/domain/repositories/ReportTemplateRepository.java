package com.innovacorp.reliant.platform.reporting.domain.repositories;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;

import java.util.List;
import java.util.Optional;

public interface ReportTemplateRepository {
    ReportTemplate save(ReportTemplate template);

    Optional<ReportTemplate> findById(Long id);

    List<ReportTemplate> findAllByOrganizationId(OrganizationId organizationId, ReportKind reportKind);

    List<ReportTemplate> findDefaultsByOrganizationIdAndKind(OrganizationId organizationId, ReportKind reportKind);

    boolean existsByOrganizationId(OrganizationId organizationId);

    boolean existsByOrganizationIdAndName(OrganizationId organizationId, String name, Long excludingId);
}
