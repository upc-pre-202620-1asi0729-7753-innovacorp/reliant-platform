package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.reporting.domain.model.aggregates.ReportTemplate;
import com.innovacorp.reliant.platform.reporting.domain.model.entities.ReportSection;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.HexColor;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportWidget;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities.ReportSectionPersistenceEntity;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities.ReportTemplatePersistenceEntity;
import com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities.ReportWidgetPersistenceEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** Static assembler between template domain and persistence representations (aggregate + sections + widgets). */
public final class ReportTemplatePersistenceAssembler {

    private ReportTemplatePersistenceAssembler() {
    }

    public static ReportTemplate toDomainFromPersistence(ReportTemplatePersistenceEntity e) {
        if (e == null) return null;
        return ReportTemplate.reconstitute(e.getId(), e.getOrganizationId(), e.getOwnerUserId(), e.getReportKind(), e.getName(), e.getDescription(),
                new HexColor(e.getPrimaryColor()), new HexColor(e.getSecondaryColor()), e.getHeaderText(), e.getFooterText(), e.getPageSize(),
                e.getOrientation(), e.isShowSignatureBlock(), e.isDefaultForKind(), e.getStatus(),
                e.getSections().stream().map(ReportTemplatePersistenceAssembler::toDomainFromPersistence).toList());
    }

    public static ReportTemplatePersistenceEntity toPersistenceFromDomain(ReportTemplate t) {
        if (t == null) return null;
        var entity = new ReportTemplatePersistenceEntity();
        if (t.getId() != null) {
            entity.setId(t.getId());
        }
        entity.setOrganizationId(t.getOrganizationId());
        entity.setOwnerUserId(t.getOwnerUserId());
        entity.setName(t.getName());
        entity.setDescription(t.getDescription());
        entity.setReportKind(t.getReportKind());
        entity.setPrimaryColor(t.getPrimaryColor().value());
        entity.setSecondaryColor(t.getSecondaryColor().value());
        entity.setHeaderText(t.getHeaderText());
        entity.setFooterText(t.getFooterText());
        entity.setPageSize(t.getPageSize());
        entity.setOrientation(t.getOrientation());
        entity.setShowSignatureBlock(t.isShowSignatureBlock());
        entity.setDefaultForKind(t.isDefaultForKind());
        entity.setStatus(t.getStatus());
        entity.setSections(t.getSections().stream().map(s -> toPersistenceFromDomain(s, entity)).collect(Collectors.toCollection(ArrayList::new)));
        return entity;
    }

    private static ReportSection toDomainFromPersistence(ReportSectionPersistenceEntity e) {
        return ReportSection.reconstitute(e.getId(), e.getTitle(), e.getSectionOrder(), e.getWidgets().stream()
                .map(w -> new ReportWidget(w.getPosition(), w.getVariableSource(), w.getVariableCode(), w.getVariableLabel(), w.getViewType(),
                        w.getAggregation(), w.getWidth()))
                .toList());
    }

    private static ReportSectionPersistenceEntity toPersistenceFromDomain(ReportSection s, ReportTemplatePersistenceEntity template) {
        var e = new ReportSectionPersistenceEntity();
        if (s.getId() != null) {
            e.setId(s.getId());
        }
        e.setTemplate(template);
        e.setTitle(s.getTitle());
        e.setSectionOrder(s.getSectionOrder());
        e.setWidgets(s.getWidgets().stream().map(w -> toPersistenceFromDomain(w, e)).collect(Collectors.toCollection(ArrayList::new)));
        return e;
    }

    private static ReportWidgetPersistenceEntity toPersistenceFromDomain(ReportWidget w, ReportSectionPersistenceEntity section) {
        var e = new ReportWidgetPersistenceEntity();
        e.setSection(section);
        e.setPosition(w.position());
        e.setVariableSource(w.variableSource());
        e.setVariableCode(w.variableCode());
        e.setVariableLabel(w.variableLabel());
        e.setViewType(w.viewType());
        e.setAggregation(w.aggregation());
        e.setWidth(w.width());
        return e;
    }
}
