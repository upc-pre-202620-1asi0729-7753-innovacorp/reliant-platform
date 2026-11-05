package com.innovacorp.reliant.platform.equipment.domain.model.events;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;

public record RecipePublishedEvent(
        Long recipeId,
        Long hvofSystemId,
        Integer recipeNumber
) {
    public static RecipePublishedEvent from(Recipe recipe) {
        return new RecipePublishedEvent(
                recipe.getId(),
                recipe.getHvofSystemIdValue(),
                recipe.getRecipeNumber()
        );
    }
}
