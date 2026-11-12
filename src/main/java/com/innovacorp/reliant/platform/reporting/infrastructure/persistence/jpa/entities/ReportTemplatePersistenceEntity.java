package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.PageOrientation;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.PageSize;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportKind;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.TemplateStatus;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_templates", uniqueConstraints = @UniqueConstraint(name = "uk_report_template_org_name", columnNames = {"organization_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
public class ReportTemplatePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_kind", nullable = false, length = 30)
    private ReportKind reportKind;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor;

    @Column(name = "secondary_color", nullable = false, length = 7)
    private String secondaryColor;

    @Column(name = "header_text", nullable = false, length = 160)
    private String headerText;

    @Column(name = "footer_text", nullable = false, length = 160)
    private String footerText;

    @Enumerated(EnumType.STRING)
    @Column(name = "page_size", nullable = false, length = 10)
    private PageSize pageSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "orientation", nullable = false, length = 10)
    private PageOrientation orientation;

    @Column(name = "show_signature_block", nullable = false)
    private boolean showSignatureBlock;

    @Column(name = "default_for_kind", nullable = false)
    private boolean defaultForKind;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private TemplateStatus status;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sectionOrder asc")
    private List<ReportSectionPersistenceEntity> sections = new ArrayList<>();
}
