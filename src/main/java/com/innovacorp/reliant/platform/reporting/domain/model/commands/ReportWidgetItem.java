package com.innovacorp.reliant.platform.reporting.domain.model.commands;

/** A widget as it travels inside the create and update commands (enumerations as lowercase strings). */
public record ReportWidgetItem(Integer position, String variableSource, String variableCode, String variableLabel, String viewType, String aggregation,
                               String width) {
}
