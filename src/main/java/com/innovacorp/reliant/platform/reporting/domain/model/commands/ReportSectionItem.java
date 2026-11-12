package com.innovacorp.reliant.platform.reporting.domain.model.commands;

import java.util.List;

public record ReportSectionItem(String title, Integer sectionOrder, List<ReportWidgetItem> widgets) {
}
