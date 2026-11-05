package com.innovacorp.reliant.platform.equipment.interfaces.rest;

import com.innovacorp.reliant.platform.equipment.application.commandservices.RecipeCommandService;
import com.innovacorp.reliant.platform.equipment.application.queryservices.RecipeQueryService;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.PublishRecipeCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllRecipesByHvofSystemIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetRecipeByIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.CreateRecipeResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RecipeResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.CreateRecipeCommandFromResourceAssembler;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.transform.RecipeResourceFromEntityAssembler;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.innovacorp.reliant.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes recipe resources.
 * Publication is a sub-resource (PATCH /{id}/publication).
 */
@RestController
@RequestMapping(value = "/api/v1/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recipes", description = "Spray recipe management endpoints")
public class RecipesController {

    private final RecipeCommandService recipeCommandService;
    private final RecipeQueryService recipeQueryService;

    public RecipesController(
            RecipeCommandService recipeCommandService,
            RecipeQueryService recipeQueryService) {
        this.recipeCommandService = recipeCommandService;
        this.recipeQueryService = recipeQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a draft recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created",
                    content = @Content(schema = @Schema(implementation = RecipeResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or unordered bands"),
            @ApiResponse(responseCode = "404", description = "HVOF system not found"),
            @ApiResponse(responseCode = "409", description = "A draft with that number already exists")
    })
    public ResponseEntity<?> createRecipe(
            @Valid @RequestBody CreateRecipeResource resource,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        var command = CreateRecipeCommandFromResourceAssembler
                .toCommandFromResource(resource);

        var result = recipeCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecipeResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "Get a recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe found",
                    content = @Content(schema = @Schema(implementation = RecipeResource.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    public ResponseEntity<?> getRecipeById(
            @PathVariable Long recipeId,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        var recipe = recipeQueryService.handle(
                new GetRecipeByIdQuery(recipeId)
        );

        if (recipe.isEmpty()) {
            return ProblemDetailFromApplicationErrorAssembler
                    .toResponseFromApplicationError(
                            ApplicationError.notFound("recipe", recipeId.toString())
                    );
        }

        return ResponseEntity.ok(
                RecipeResourceFromEntityAssembler
                        .toResourceFromEntity(recipe.get())
        );
    }

    @GetMapping
    @Operation(summary = "Get all recipes of an HVOF system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes retrieved",
                    content = @Content(schema = @Schema(implementation = RecipeResource.class)))
    })
    public ResponseEntity<List<RecipeResource>> getAllRecipesByHvofSystemId(
            @RequestParam Long hvofSystemId,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        var recipes = recipeQueryService.handle(
                new GetAllRecipesByHvofSystemIdQuery(
                        new HvofSystemId(hvofSystemId)
                )
        );

        return ResponseEntity.ok(
                recipes.stream()
                        .map(RecipeResourceFromEntityAssembler::toResourceFromEntity)
                        .toList()
        );
    }

    @PatchMapping("/{recipeId}/publication")
    @Operation(
            summary = "Publish a recipe",
            description = "DRAFT → ACTIVE. The previous ACTIVE recipe with the same number is retired."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe published",
                    content = @Content(schema = @Schema(implementation = RecipeResource.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found"),
            @ApiResponse(responseCode = "422", description = "Recipe is not a draft")
    })
    public ResponseEntity<?> publishRecipe(
            @PathVariable Long recipeId,
            @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage) {

        var result = recipeCommandService.handle(
                new PublishRecipeCommand(recipeId)
        );

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RecipeResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}