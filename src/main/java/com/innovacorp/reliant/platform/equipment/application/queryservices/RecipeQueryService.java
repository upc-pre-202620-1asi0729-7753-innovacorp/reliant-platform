package com.innovacorp.reliant.platform.equipment.application.queryservices;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetActiveRecipeByHvofSystemIdAndNumberQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllRecipesByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetRecipeByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RecipeQueryService {

    Optional<Recipe> handle(GetRecipeByIdQuery query);

    List<Recipe> handle(GetAllRecipesByHvofSystemIdQuery query);

    Optional<Recipe> handle(GetActiveRecipeByHvofSystemIdAndNumberQuery query);
}
