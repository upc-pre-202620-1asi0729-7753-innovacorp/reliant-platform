package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

/** One block of a section: a variable, how it is aggregated and how it is drawn. */
public record ReportWidget(int position, VariableSource variableSource, String variableCode, String variableLabel, WidgetViewType viewType,
                           Aggregation aggregation, WidgetWidth width) {
    public ReportWidget {
        if (position < 1) throw new IllegalArgumentException("Widget position must be positive");
        if (variableCode == null || !variableCode.trim().matches("^[a-z][a-z0-9_]{2,39}$")) {
            throw new IllegalArgumentException("Variable code must be snake_case, e.g. fuel_gas_flow");
        }
        if (variableLabel == null || variableLabel.isBlank()) throw new IllegalArgumentException("Variable label must not be blank");
        if (variableSource == null || viewType == null || aggregation == null || width == null) {
            throw new IllegalArgumentException("Widget source, view type, aggregation and width are required");
        }
        variableCode = variableCode.trim();
        variableLabel = variableLabel.trim();
    }
}
