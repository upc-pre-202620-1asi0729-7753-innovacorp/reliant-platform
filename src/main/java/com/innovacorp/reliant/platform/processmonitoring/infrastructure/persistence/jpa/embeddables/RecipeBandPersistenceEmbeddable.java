package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeBandPersistenceEmbeddable {

    @Column(name = "parameter", nullable = false, length = 40)
    private String parameter;

    @Column(name = "setpoint", nullable = false)
    private double setpoint;

    @Column(name = "lower_shutdown", nullable = false)
    private double lowerShutdown;

    @Column(name = "lower_warning", nullable = false)
    private double lowerWarning;

    @Column(name = "nominal_min", nullable = false)
    private double nominalMin;

    @Column(name = "nominal_max", nullable = false)
    private double nominalMax;

    @Column(name = "upper_warning", nullable = false)
    private double upperWarning;

    @Column(name = "upper_shutdown", nullable = false)
    private double upperShutdown;

    @Column(name = "unit_symbol", nullable = false, length = 10)
    private String unitSymbol;

    @Column(name = "unit_category", nullable = false, length = 20)
    private String unitCategory;
}