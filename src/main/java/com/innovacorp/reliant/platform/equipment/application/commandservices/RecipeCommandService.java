package com.innovacorp.reliant.platform.equipment.application.commandservices;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.CreateRecipeCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.PublishRecipeCommand;

import java.util.Optional;

public interface RecipeCommandService {

    Optional<Recipe> handle(CreateRecipeCommand command);

    Optional<Recipe> handle(PublishRecipeCommand command);
}