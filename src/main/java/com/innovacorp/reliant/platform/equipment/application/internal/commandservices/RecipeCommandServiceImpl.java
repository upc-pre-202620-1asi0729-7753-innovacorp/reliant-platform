package com.innovacorp.reliant.platform.equipment.application.internal.commandservices;

import com.innovacorp.reliant.platform.equipment.application.commandservices.RecipeCommandService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.CreateRecipeCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.PublishRecipeCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeStatus;
import com.innovacorp.reliant.platform.equipment.domain.repositories.HvofSystemRepository;
import com.innovacorp.reliant.platform.equipment.domain.repositories.RecipeRepository;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Recipe Command Service Implementation.
 */
@Service
public class RecipeCommandServiceImpl implements RecipeCommandService {

    private final RecipeRepository recipeRepository;
    private final HvofSystemRepository hvofSystemRepository;

    public RecipeCommandServiceImpl(RecipeRepository recipeRepository, HvofSystemRepository hvofSystemRepository) {
        this.recipeRepository = recipeRepository;
        this.hvofSystemRepository = hvofSystemRepository;
    }

    @Override
    public Result<Recipe, ApplicationError> handle(CreateRecipeCommand command) {
        try {
            var hvofSystemId = new HvofSystemId(command.hvofSystemId());

            if (hvofSystemRepository.findById(command.hvofSystemId()).isEmpty()) {
                return Result.failure(ApplicationError.notFound("hvof-system", String.valueOf(command.hvofSystemId())));
            }

            if (recipeRepository.existsByHvofSystemIdAndRecipeNumberAndStatus(hvofSystemId, command.recipeNumber(), RecipeStatus.DRAFT)) {
                return Result.failure(ApplicationError.conflict("recipe", "DRAFT " + command.recipeNumber()));
            }

            return Result.success(recipeRepository.save(new Recipe(command)));

        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Recipe", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Recipe creation", e.getMessage()));
        }
    }

    @Override
    public Result<Recipe, ApplicationError> handle(PublishRecipeCommand command) {
        var recipe = recipeRepository.findById(command.recipeId());

        if (recipe.isEmpty()) {
            return Result.failure(ApplicationError.notFound("recipe", String.valueOf(command.recipeId())));
        }

        try {
            var target = recipe.get();

            recipeRepository.findActiveByHvofSystemIdAndRecipeNumber(target.getHvofSystemId(), target.getRecipeNumber())
                    .ifPresent(active -> {
                        active.retire();
                        recipeRepository.save(active);
                    });

            target.publish();

            return Result.success(recipeRepository.save(target));

        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("recipe-publication", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Recipe publication", e.getMessage()));
        }
    }
}