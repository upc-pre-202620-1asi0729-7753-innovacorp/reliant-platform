package com.innovacorp.reliant.platform.reporting.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_sections", uniqueConstraints = @UniqueConstraint(name = "uk_report_section_order", columnNames = {"report_template_id", "section_order"}))
@Getter
@Setter
@NoArgsConstructor
public class ReportSectionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_template_id", nullable = false)
    private ReportTemplatePersistenceEntity template;

    @Column(name = "title", nullable = false, length = 80)
    private String title;

    @Column(name = "section_order", nullable = false)
    private int sectionOrder;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("position asc")
    private List<ReportWidgetPersistenceEntity> widgets = new ArrayList<>();
}
