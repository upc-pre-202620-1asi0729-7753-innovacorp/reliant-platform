package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.GeneratedReport;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities.GeneratedReportPersistenceEntity;

public final class GeneratedReportPersistenceAssembler {

    private GeneratedReportPersistenceAssembler() {
    }

    public static GeneratedReport toDomainFromPersistence(GeneratedReportPersistenceEntity e) {
        if (e == null) return null;
        return GeneratedReport.reconstitute(e.getId(), e.getReportTemplateId(), e.getOrganizationId(), e.getGeneratedByUserId(), e.getSubjectType(),
                e.getSubjectId(), e.getFormat(), e.getFileName(), e.getPeriodFrom(), e.getPeriodTo(), e.getGeneratedAt());
    }

    public static GeneratedReportPersistenceEntity toPersistenceFromDomain(GeneratedReport r) {
        if (r == null) return null;
        var e = new GeneratedReportPersistenceEntity();
        if (r.getId() != null) {
            e.setId(r.getId());
        }
        e.setReportTemplateId(r.getReportTemplateId());
        e.setOrganizationId(r.getOrganizationId());
        e.setGeneratedByUserId(r.getGeneratedByUserId());
        e.setSubjectType(r.getSubjectType());
        e.setSubjectId(r.getSubjectId());
        e.setFormat(r.getFormat());
        e.setFileName(r.getFileName());
        e.setPeriodFrom(r.getPeriodFrom());
        e.setPeriodTo(r.getPeriodTo());
        e.setGeneratedAt(r.getGeneratedAt());
        return e;
    }
}
