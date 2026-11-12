package com.innovacorp.reliant.platform.equipment.domain.model.events;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;

public record RecipeCreatedEvent(
        Long recipeId,
        Long hvofSystemId,
        Integer recipeNumber
) {
    public static RecipeCreatedEvent from(Recipe recipe) {
        return new RecipeCreatedEvent(
                recipe.getId(),
                recipe.getHvofSystemIdValue(),
                recipe.getRecipeNumber()
        );
    }
}
