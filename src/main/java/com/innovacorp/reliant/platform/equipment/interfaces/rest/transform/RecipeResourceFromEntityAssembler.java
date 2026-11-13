package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RecipeApplicabilityResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RecipeParameterResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RecipeResource;

public class RecipeResourceFromEntityAssembler {

    public static RecipeResource toResourceFromEntity(Recipe entity) {

        var applicabilities = entity.getApplicabilities().stream()
                .map(a -> new RecipeApplicabilityResource(
                        a.componentType(),
                        a.machineModel(),
                        a.position()
                ))
                .toList();

        var parameters = entity.getParameters().stream()
                .map(p -> new RecipeParameterResource(
                        p.getParameter(),
                        p.getSetpoint(),
                        p.getBands().lowerShutdown(),
                        p.getBands().lowerWarning(),
                        p.getBands().nominalMin(),
                        p.getBands().nominalMax(),
                        p.getBands().upperWarning(),
                        p.getBands().upperShutdown(),
                        p.getUnitSymbol(),
                        p.getUnitCategory().toString().toLowerCase()
                ))
                .toList();

        return new RecipeResource(
                entity.getId(),
                entity.getHvofSystemIdValue(),
                entity.getRecipeNumber(),
                entity.getName(),
                entity.getPowderSpecification(),
                entity.getStatus().name(),
                applicabilities,
                parameters
        );
    }
}