package com.innovacorp.reliant.platform.equipment.application.internal.queryservices;

import com.innovacorp.reliant.platform.equipment.application.queryservices.RecipeQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetActiveRecipeByHvofSystemIdAndNumberQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllRecipesByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetRecipeByIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeQueryServiceImpl implements RecipeQueryService {

    private final RecipeRepository recipeRepository;

    public RecipeQueryServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Optional<Recipe> handle(GetRecipeByIdQuery query) {
        return recipeRepository.findById(query.recipeId());
    }

    @Override
    public List<Recipe> handle(GetAllRecipesByHvofSystemIdQuery query) {
        return recipeRepository.findAllByHvofSystemId(query.hvofSystemId());
    }

    @Override
    public Optional<Recipe> handle(GetActiveRecipeByHvofSystemIdAndNumberQuery query) {
        return recipeRepository.findActiveByHvofSystemIdAndRecipeNumber(
                query.hvofSystemId(),
                query.recipeNumber()
        );
    }
}