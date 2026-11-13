package com.innovacorp.reliant.platform.equipment.application.commandservices;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.CreateRecipeCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.PublishRecipeCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

public interface RecipeCommandService {

    Result<Recipe, ApplicationError> handle(CreateRecipeCommand command);

    Result<Recipe, ApplicationError> handle(PublishRecipeCommand command);
}