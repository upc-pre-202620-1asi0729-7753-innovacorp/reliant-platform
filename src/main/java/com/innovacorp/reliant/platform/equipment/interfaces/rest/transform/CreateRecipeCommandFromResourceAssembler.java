package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.CreateRecipeCommand;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.CreateRecipeResource;

import java.util.List;

public class CreateRecipeCommandFromResourceAssembler {

    public static CreateRecipeCommand toCommandFromResource(CreateRecipeResource resource) {

        var applicabilities = resource.applicabilities() == null
                ? List.<CreateRecipeCommand.ApplicabilityItem>of()
                : resource.applicabilities().stream()
                .map(a -> new CreateRecipeCommand.ApplicabilityItem(
                        a.componentType(),
                        a.machineModel(),
                        a.position()
                ))
                .toList();

        var parameters = resource.parameters().stream()
                .map(p -> new CreateRecipeCommand.ParameterItem(
                        p.parameter(),
                        p.setpoint(),
                        p.lowerShutdown(),
                        p.lowerWarning(),
                        p.nominalMin(),
                        p.nominalMax(),
                        p.upperWarning(),
                        p.upperShutdown(),
                        p.unitSymbol(),
                        p.unitCategory()
                ))
                .toList();

        return new CreateRecipeCommand(
                resource.hvofSystemId(),
                resource.recipeNumber(),
                resource.name(),
                resource.powderSpecification(),
                applicabilities,
                parameters
        );
    }
}
