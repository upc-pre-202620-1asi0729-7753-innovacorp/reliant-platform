package com.innovacorp.reliant.platform.equipment.domain.model.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.ParameterBands;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.UnitCategory;
import lombok.Getter;

@Getter
public class RecipeParameter {

    private Long id;
    private String parameter;
    private double setpoint;
    private ParameterBands bands;
    private String unitSymbol;
    private UnitCategory unitCategory;

    public RecipeParameter(
            Long id,
            String parameter,
            double setpoint,
            ParameterBands bands,
            String unitSymbol,
            UnitCategory unitCategory
    ) {
        if (parameter == null || !parameter.matches("^[a-z][a-z0-9_]{2,39}$")) {
            throw new IllegalArgumentException("Invalid parameter name");
        }
        if (bands == null) {
            throw new IllegalArgumentException("Parameter bands are required");
        }
        if (!bands.contains(setpoint)) {
            throw new IllegalArgumentException("Setpoint must be within nominal limits");
        }
        if (unitSymbol == null || unitSymbol.isBlank()) {
            throw new IllegalArgumentException("Unit symbol is required");
        }

        this.id = id;
        this.parameter = parameter;
        this.setpoint = setpoint;
        this.bands = bands;
        this.unitSymbol = unitSymbol.trim();
        this.unitCategory = unitCategory == null
                ? UnitCategory.DIMENSIONLESS
                : unitCategory;
    }

    public static RecipeParameter reconstitute(
            Long id,
            String parameter,
            double setpoint,
            ParameterBands bands,
            String unitSymbol,
            UnitCategory unitCategory
    ) {
        return new RecipeParameter(
                id, parameter, setpoint, bands, unitSymbol, unitCategory
        );
    }
}
