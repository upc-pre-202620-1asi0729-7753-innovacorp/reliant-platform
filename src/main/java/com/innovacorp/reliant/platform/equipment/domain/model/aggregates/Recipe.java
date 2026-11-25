package com.innovacorp.reliant.platform.equipment.domain.model.aggregates;

import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.CreateRecipeCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.RecipeParameter;
import com.innovacorp.reliant.platform.equipment.domain.model.events.RecipeCreatedEvent;
import com.innovacorp.reliant.platform.equipment.domain.model.events.RecipePublishedEvent;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeApplicability;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe aggregate root: the process window (setpoint and bands per parameter) of an HVOF system, published DRAFT -> ACTIVE -> RETIRED.
 */
@Getter
public class Recipe extends AbstractDomainAggregateRoot<Recipe> {

    private Long id;
    private final HvofSystemId hvofSystemId;
    private final Integer recipeNumber;
    private final String name;
    private final String powderSpecification;
    private RecipeStatus status;
    private final List<RecipeApplicability> applicabilities;
    private final List<RecipeParameter> parameters;

    public Recipe(
            HvofSystemId hvofSystemId,
            Integer recipeNumber,
            String name,
            String powderSpecification,
            List<RecipeApplicability> applicabilities,
            List<RecipeParameter> parameters
    ) {
        if (hvofSystemId == null) {
            throw new IllegalArgumentException("validation.recipe.hvof-system-id.required");
        }
        if (recipeNumber == null || recipeNumber <= 0) {
            throw new IllegalArgumentException("validation.recipe-number.positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("validation.recipe.name.blank");
        }
        if (powderSpecification == null || powderSpecification.isBlank()) {
            throw new IllegalArgumentException("validation.recipe.powder-specification.blank");
        }

        this.hvofSystemId = hvofSystemId;
        this.recipeNumber = recipeNumber;
        this.name = name.trim();
        this.powderSpecification = powderSpecification.trim();
        this.status = RecipeStatus.DRAFT;
        this.applicabilities = applicabilities == null
                ? new ArrayList<>()
                : new ArrayList<>(applicabilities);
        this.parameters = parameters == null
                ? new ArrayList<>()
                : new ArrayList<>(parameters);
    }

    public Recipe(CreateRecipeCommand command) {
        this(
                new HvofSystemId(command.hvofSystemId()),
                command.recipeNumber(),
                command.name(),
                command.powderSpecification(),
                command.applicabilities() == null ? List.of() :
                        command.applicabilities().stream()
                                .map(item -> new RecipeApplicability(
                                        item.componentType(),
                                        item.machineModel(),
                                        item.position()
                                )).toList(),
                command.parameters() == null ? List.of() :
                        command.parameters().stream()
                                .map(item -> new RecipeParameter(
                                        null,
                                        item.parameter(),
                                        item.setpoint(),
                                        new com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.ParameterBands(
                                                item.lowerShutdown(),
                                                item.lowerWarning(),
                                                item.nominalMin(),
                                                item.nominalMax(),
                                                item.upperWarning(),
                                                item.upperShutdown()
                                        ),
                                        item.unitSymbol(),
                                        item.unitCategory() == null ? null :
                                                com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.UnitCategory.fromString(item.unitCategory())
                                )).toList()
        );
    }

    public static Recipe reconstitute(
            Long id,
            HvofSystemId hvofSystemId,
            Integer recipeNumber,
            String name,
            String powderSpecification,
            RecipeStatus status,
            List<RecipeApplicability> applicabilities,
            List<RecipeParameter> parameters
    ) {
        Recipe recipe = new Recipe(
                hvofSystemId,
                recipeNumber,
                name,
                powderSpecification,
                applicabilities,
                parameters
        );
        recipe.id = id;
        recipe.status = status;
        return recipe;
    }

    public boolean isDraft() {
        return status == RecipeStatus.DRAFT;
    }

    public boolean isActive() {
        return status == RecipeStatus.ACTIVE;
    }

    public void publish() {
        if (!isDraft()) {
            throw new IllegalStateException("Only draft recipes can be published");
        }
        this.status = RecipeStatus.ACTIVE;
        registerDomainEvent(RecipePublishedEvent.from(this));
    }

    public void retire() {
        if (status != RecipeStatus.ACTIVE) {
            throw new IllegalStateException("Only active recipes can be retired");
        }
        this.status = RecipeStatus.RETIRED;
    }

    public RecipeParameter parameterFor(String parameter) {
        return parameters.stream()
                .filter(item -> item.getParameter().equals(parameter))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("validation.recipe.parameter.not-found"));
    }

    public Long getHvofSystemIdValue() {
        return hvofSystemId.hvofSystemId();
    }

    /** Called by the repository adapter once the id is known, so the creation event carries it. Never on reconstitution. */
    public void onCreated() {
        registerDomainEvent(RecipeCreatedEvent.from(this));
    }
}