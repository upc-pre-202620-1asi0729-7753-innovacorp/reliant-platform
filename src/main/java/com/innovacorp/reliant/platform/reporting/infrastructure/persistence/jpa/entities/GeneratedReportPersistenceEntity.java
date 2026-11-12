package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportSubjectType;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "generated_reports", indexes = @Index(name = "ix_generated_report_subject", columnList = "organization_id, subject_type, subject_id"))
@Getter
@Setter
@NoArgsConstructor
public class GeneratedReportPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "report_template_id", nullable = false)
    private Long reportTemplateId;

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Column(name = "generated_by_user_id", nullable = false)
    private Long generatedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type", nullable = false, length = 20)
    private ReportSubjectType subjectType;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "format", nullable = false, length = 10)
    private String format;

    @Column(name = "file_name", nullable = false, length = 160)
    private String fileName;

    @Column(name = "period_from")
    private LocalDate periodFrom;

    @Column(name = "period_to")
    private LocalDate periodTo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generated_at", nullable = false)
    private Date generatedAt;
}
