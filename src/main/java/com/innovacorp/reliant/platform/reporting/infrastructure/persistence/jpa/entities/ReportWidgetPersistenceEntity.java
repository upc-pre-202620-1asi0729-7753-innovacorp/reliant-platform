package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.Aggregation;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.VariableSource;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.WidgetViewType;
import com.innovacorp.reliant.platform.reporting.domain.model.valueobjects.WidgetWidth;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report_widgets", uniqueConstraints = @UniqueConstraint(name = "uk_report_widget_position", columnNames = {"report_section_id", "position"}))
@Getter
@Setter
@NoArgsConstructor
public class ReportWidgetPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_section_id", nullable = false)
    private ReportSectionPersistenceEntity section;

    @Column(name = "position", nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(name = "variable_source", nullable = false, length = 20)
    private VariableSource variableSource;

    @Column(name = "variable_code", nullable = false, length = 40)
    private String variableCode;

    @Column(name = "variable_label", nullable = false, length = 80)
    private String variableLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_type", nullable = false, length = 10)
    private WidgetViewType viewType;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregation", nullable = false, length = 10)
    private Aggregation aggregation;

    @Enumerated(EnumType.STRING)
    @Column(name = "width", nullable = false, length = 10)
    private WidgetWidth width;
}
