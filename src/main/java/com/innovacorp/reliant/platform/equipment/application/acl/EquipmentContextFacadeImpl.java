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

    public EquipmentContextFacadeImpl(HvofSystemQueryService hvofSystemQueryService, RecipeQueryService recipeQueryService) {
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
        return recipeQueryService.handle(new GetActiveRecipeByHvofSystemIdAndNumberQuery(new HvofSystemId(hvofSystemId), recipeNumber))
                .map(recipe -> recipe.getParameters().stream()
                        .map(p -> new RecipeBandSnapshot(p.getParameter(), p.getSetpoint(), p.getBands().lowerShutdown(), p.getBands().lowerWarning(),
                                p.getBands().nominalMin(), p.getBands().nominalMax(), p.getBands().upperWarning(), p.getBands().upperShutdown(),
                                p.getUnitSymbol(), p.getUnitCategory().toLowerCase()))
                        .toList())
                .orElse(List.of());
    }
}
