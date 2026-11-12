package com.innovacorp.reliant.platform.equipment.application.acl;

import com.innovacorp.reliant.platform.equipment.application.queryservices.HvofSystemQueryService;
import com.innovacorp.reliant.platform.equipment.application.queryservices.RecipeQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetActiveRecipeByHvofSystemIdAndNumberQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.interfaces.acl.EquipmentContextFacade;
import com.innovacorp.reliant.platform.equipment.interfaces.acl.RecipeBandSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EquipmentContextFacadeImpl implements EquipmentContextFacade {

    private final HvofSystemQueryService hvofSystemQueryService;
    private final RecipeQueryService recipeQueryService;

    public EquipmentContextFacadeImpl(HvofSystemQueryService hvofSystemQueryService,
                                      RecipeQueryService recipeQueryService) {
        this.hvofSystemQueryService = hvofSystemQueryService;
        this.recipeQueryService = recipeQueryService;
    }

    @Override
    public Long fetchHvofSystemOrganizationId(Long hvofSystemId) {
        return hvofSystemQueryService.handle(new GetHvofSystemByIdQuery(hvofSystemId))
                .map(HvofSystem::getOrganizationIdValue)
                .orElse(0L);
    }

    @Override
    public List<RecipeBandSnapshot> fetchActiveRecipeBands(Long hvofSystemId, Integer recipeNumber) {
        var query = new GetActiveRecipeByHvofSystemIdAndNumberQuery(
                new HvofSystemId(hvofSystemId), recipeNumber);
        return recipeQueryService.handle(query)
                .map(recipe -> recipe.getParameters().stream()
                        .map(parameter -> {
                            var bands = parameter.getBands();
                            return new RecipeBandSnapshot(
                                    parameter.getParameter(),
                                    parameter.getSetpoint(),
                                    bands.lowerShutdown(),
                                    bands.lowerWarning(),
                                    bands.nominalMin(),
                                    bands.nominalMax(),
                                    bands.upperWarning(),
                                    bands.upperShutdown(),
                                    parameter.getUnitSymbol(),
                                    parameter.getUnitCategory().toLowerCase());
                        })
                        .toList())
                .orElse(List.of());
    }
}