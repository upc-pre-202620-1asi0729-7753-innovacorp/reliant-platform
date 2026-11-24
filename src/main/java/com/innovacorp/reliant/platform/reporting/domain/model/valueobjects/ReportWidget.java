package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

/** One block of a section: a variable, how it is aggregated and how it is drawn. */
public record ReportWidget(int position, VariableSource variableSource, String variableCode, String variableLabel, WidgetViewType viewType,
                           Aggregation aggregation, WidgetWidth width) {
    public ReportWidget {
        if (position < 1) throw new IllegalArgumentException("validation.report-widget.position.positive");
        if (variableCode == null || !variableCode.trim().matches("^[a-z][a-z0-9_]{2,39}$")) {
            throw new IllegalArgumentException("validation.report-widget.variable-code.format");
        }
        if (variableLabel == null || variableLabel.isBlank()) throw new IllegalArgumentException("validation.report-widget.variable-label.blank");
        if (variableSource == null || viewType == null || aggregation == null || width == null) {
            throw new IllegalArgumentException("validation.report-widget.required-fields");
        }
        variableCode = variableCode.trim();
        variableLabel = variableLabel.trim();
    }
}