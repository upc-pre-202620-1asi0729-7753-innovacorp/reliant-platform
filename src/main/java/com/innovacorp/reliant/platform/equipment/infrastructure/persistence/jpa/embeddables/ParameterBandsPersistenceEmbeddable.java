package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence representation of ParameterBands (six columns in the recipe_parameters table).
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParameterBandsPersistenceEmbeddable {

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
}
