package com.innovacorp.reliant.platform.reporting.domain.model.aggregates;

import com.innovacorp.reliant.platform.reporting.domain.model.commands.RegisterGeneratedReportCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.events.ReportGeneratedEvent;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * The record of a report rendered from a template: who generated it, about what and when.
 */
@Getter
public class GeneratedReport extends AbstractDomainAggregateRoot<GeneratedReport> {

    @Setter
    private Long id;
    private final Long reportTemplateId;
    private final OrganizationId organizationId;
    private final Long generatedByUserId;
    private final ReportSubjectType subjectType;
    private final Long subjectId;
    private final String format;
    private final String fileName;
    private final LocalDate periodFrom;
    private final LocalDate periodTo;
    private final Date generatedAt;

    public GeneratedReport(Long id, Long reportTemplateId, OrganizationId organizationId, Long generatedByUserId, ReportSubjectType subjectType, Long subjectId,
                           String format, String fileName, LocalDate periodFrom, LocalDate periodTo, Date generatedAt) {
        if (reportTemplateId == null || reportTemplateId <= 0) throw new IllegalArgumentException("Report template id must be positive");
        if (subjectId == null || subjectId <= 0) throw new IllegalArgumentException("Subject id must be positive");
        if (fileName == null || fileName.isBlank()) throw new IllegalArgumentException("File name must not be blank");
        if (periodFrom != null && periodTo != null && periodTo.isBefore(periodFrom)) throw new IllegalArgumentException("The period end cannot precede its start");
        this.id = id;
        this.reportTemplateId = reportTemplateId;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.generatedByUserId = Objects.requireNonNull(generatedByUserId, "generatedByUserId must not be null");
        this.subjectType = Objects.requireNonNull(subjectType, "subjectType must not be null");
        this.subjectId = subjectId;
        this.format = format == null ? "PDF" : format;
        this.fileName = fileName.trim();
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.generatedAt = generatedAt == null ? new Date() : generatedAt;
    }

    public GeneratedReport(RegisterGeneratedReportCommand c) {
        this(null, c.reportTemplateId(), new OrganizationId(c.organizationId()), c.generatedByUserId(), ReportSubjectType.fromString(c.subjectType()),
                c.subjectId(), "PDF", c.fileName(), c.periodFrom(), c.periodTo(), new Date());
    }

    public static GeneratedReport reconstitute(Long id, Long reportTemplateId, OrganizationId organizationId, Long generatedByUserId, ReportSubjectType subjectType,
                                               Long subjectId, String format, String fileName, LocalDate periodFrom, LocalDate periodTo, Date generatedAt) {
        return new GeneratedReport(id, reportTemplateId, organizationId, generatedByUserId, subjectType, subjectId, format, fileName, periodFrom, periodTo, generatedAt);
    }

    /** Called by the repository once the id is known, so the event carries it. */
    public void onRegistered() {
        registerDomainEvent(new ReportGeneratedEvent(id, reportTemplateId, organizationId.organizationId(), subjectType.name().toLowerCase(), subjectId));
    }

    public boolean isOwnedBy(Long organizationId) { return this.organizationId.organizationId().equals(organizationId); }

    public Long getOrganizationIdValue() { return organizationId.organizationId(); }
}
