package com.innovacorp.reliant.platform.equipment.domain.repositories;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeStatus;

import java.util.List;
import java.util.Optional;

/**
 * Recipe repository port.
 */
public interface RecipeRepository {

    Recipe save(Recipe recipe);

    Optional<Recipe> findById(Long id);

    List<Recipe> findAllByHvofSystemId(HvofSystemId hvofSystemId);

    boolean existsByHvofSystemIdAndRecipeNumberAndStatus(
            HvofSystemId hvofSystemId,
            Integer recipeNumber,
            RecipeStatus status
    );

    Optional<Recipe> findActiveByHvofSystemIdAndRecipeNumber(
            HvofSystemId hvofSystemId,
            Integer recipeNumber
    );
}
