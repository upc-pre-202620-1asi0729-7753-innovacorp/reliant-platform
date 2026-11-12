package com.innovacorp.reliant.platform.reporting.domain.model.aggregates;

import com.innovacorp.reliant.platform.reporting.domain.model.commands.CreateReportTemplateCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.ReportSectionItem;
import com.innovacorp.reliant.platform.reporting.domain.model.commands.UpdateReportTemplateCommand;
import com.innovacorp.reliant.platform.reporting.domain.model.entities.ReportSection;
import com.innovacorp.reliant.platform.reporting.domain.model.events.ReportTemplateCreatedEvent;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.*;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A customizable report layout of an organization: branding, page setup and ordered sections of widgets.
 */
@Getter
public class ReportTemplate extends AbstractDomainAggregateRoot<ReportTemplate> {

    @Setter
    private Long id;
    private final OrganizationId organizationId;
    private final Long ownerUserId;
    private final ReportKind reportKind;
    private String name;
    private String description;
    private HexColor primaryColor;
    private HexColor secondaryColor;
    private String headerText;
    private String footerText;
    private PageSize pageSize;
    private PageOrientation orientation;
    private boolean showSignatureBlock;
    private boolean defaultForKind;
    private TemplateStatus status;
    private List<ReportSection> sections;

    private ReportTemplate(Long id, OrganizationId organizationId, Long ownerUserId, ReportKind reportKind, TemplateStatus status) {
        this.id = id;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.ownerUserId = Objects.requireNonNull(ownerUserId, "ownerUserId must not be null");
        this.reportKind = Objects.requireNonNull(reportKind, "reportKind must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public ReportTemplate(CreateReportTemplateCommand c) {
        this(null, new OrganizationId(c.organizationId()), c.ownerUserId(), ReportKind.fromString(c.reportKind()), TemplateStatus.ACTIVE);
        apply(c.name(), c.description(), c.primaryColor(), c.secondaryColor(), c.headerText(), c.footerText(), c.pageSize(), c.orientation(),
                c.showSignatureBlock(), c.defaultForKind(), c.sections());
    }

    public static ReportTemplate reconstitute(Long id, OrganizationId organizationId, Long ownerUserId, ReportKind reportKind, String name, String description,
                                              HexColor primaryColor, HexColor secondaryColor, String headerText, String footerText, PageSize pageSize,
                                              PageOrientation orientation, boolean showSignatureBlock, boolean defaultForKind, TemplateStatus status,
                                              List<ReportSection> sections) {
        var template = new ReportTemplate(id, organizationId, ownerUserId, reportKind, status);
        template.name = name;
        template.description = description == null ? "" : description;
        template.primaryColor = primaryColor;
        template.secondaryColor = secondaryColor;
        template.headerText = headerText == null ? "" : headerText;
        template.footerText = footerText == null ? "" : footerText;
        template.pageSize = pageSize;
        template.orientation = orientation;
        template.showSignatureBlock = showSignatureBlock;
        template.defaultForKind = defaultForKind;
        template.sections = sections == null ? List.of() : sections.stream().sorted(Comparator.comparingInt(ReportSection::getSectionOrder)).toList();
        return template;
    }

    /** Called by the repository once the id is known, so the event carries it. */
    public void onCreated() {
        registerDomainEvent(new ReportTemplateCreatedEvent(id, organizationId.organizationId(), reportKind.name().toLowerCase()));
    }

    /** Replaces the layout. Archived templates are read-only. */
    public void update(UpdateReportTemplateCommand c) {
        if (status != TemplateStatus.ACTIVE) throw new IllegalStateException("Archived templates cannot be edited");
        apply(c.name(), c.description(), c.primaryColor(), c.secondaryColor(), c.headerText(), c.footerText(), c.pageSize(), c.orientation(),
                c.showSignatureBlock(), c.defaultForKind(), c.sections());
    }

    /** ACTIVE ⇄ ARCHIVED. Throws when the template is already in that status. */
    public void changeStatus(TemplateStatus newStatus) {
        if (status == newStatus) throw new IllegalStateException("The template is already " + newStatus.name().toLowerCase());
        status = newStatus;
        if (newStatus == TemplateStatus.ARCHIVED) defaultForKind = false;
    }

    /** Used when another template of the same kind becomes the default. */
    public void clearDefault() {
        defaultForKind = false;
    }

    public boolean isActive() { return status == TemplateStatus.ACTIVE; }

    public boolean isOwnedBy(Long organizationId) { return this.organizationId.organizationId().equals(organizationId); }

    public Long getOrganizationIdValue() { return organizationId.organizationId(); }

    public int getWidgetCount() { return sections.stream().mapToInt(s -> s.getWidgets().size()).sum(); }

    private void apply(String name, String description, String primaryColor, String secondaryColor, String headerText, String footerText,
                       String pageSize, String orientation, boolean showSignatureBlock, boolean defaultForKind, List<ReportSectionItem> sectionItems) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Template name must not be blank");
        if (sectionItems == null || sectionItems.isEmpty()) throw new IllegalArgumentException("A template needs at least one section");
        if (sectionItems.stream().map(ReportSectionItem::sectionOrder).distinct().count() != sectionItems.size()) {
            throw new IllegalArgumentException("Section orders must be unique");
        }
        this.name = name.trim();
        this.description = description == null ? "" : description.trim();
        this.primaryColor = new HexColor(primaryColor);
        this.secondaryColor = new HexColor(secondaryColor);
        this.headerText = headerText == null ? "" : headerText.trim();
        this.footerText = footerText == null ? "" : footerText.trim();
        this.pageSize = PageSize.fromString(pageSize);
        this.orientation = PageOrientation.fromString(orientation);
        this.showSignatureBlock = showSignatureBlock;
        this.defaultForKind = defaultForKind;
        this.sections = sectionItems.stream()
                .sorted(Comparator.comparingInt(s -> s.sectionOrder() == null ? 0 : s.sectionOrder()))
                .map(s -> new ReportSection(null, s.title(), s.sectionOrder() == null ? 0 : s.sectionOrder(),
                        s.widgets() == null ? List.of() : s.widgets().stream().map(w -> new ReportWidget(w.position() == null ? 0 : w.position(),
                                VariableSource.fromString(w.variableSource()), w.variableCode(), w.variableLabel(), WidgetViewType.fromString(w.viewType()),
                                Aggregation.fromString(w.aggregation()), WidgetWidth.fromString(w.width()))).toList()))
                .toList();
    }
}
