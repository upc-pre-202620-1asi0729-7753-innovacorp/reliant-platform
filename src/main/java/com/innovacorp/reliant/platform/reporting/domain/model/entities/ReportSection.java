package com.innovacorp.reliant.platform.reporting.domain.model.entities;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.ReportWidget;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

/** An ordered block of a template with its widgets. */
@Getter
public class ReportSection {
    private final Long id;
    private final String title;
    private final int sectionOrder;
    private final List<ReportWidget> widgets;

    public ReportSection(Long id, String title, int sectionOrder, List<ReportWidget> widgets) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("validation.report-section.title.blank");
        if (sectionOrder < 1) throw new IllegalArgumentException("validation.report-section.order.positive");
        if (widgets == null || widgets.isEmpty()) throw new IllegalArgumentException("validation.report-section.widgets.empty");
        if (widgets.stream().map(ReportWidget::position).distinct().count() != widgets.size()) {
            throw new IllegalArgumentException("validation.report-section.widget-positions.duplicate");
        }
        this.id = id;
        this.title = title.trim();
        this.sectionOrder = sectionOrder;
        this.widgets = widgets.stream().sorted(Comparator.comparingInt(ReportWidget::position)).toList();
    }

    public static ReportSection reconstitute(Long id, String title, int sectionOrder, List<ReportWidget> widgets) {
        return new ReportSection(id, title, sectionOrder, widgets);
    }
}