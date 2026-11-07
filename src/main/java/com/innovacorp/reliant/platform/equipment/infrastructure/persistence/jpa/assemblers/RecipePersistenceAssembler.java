package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.RecipeParameter;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.ParameterBands;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeApplicability;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables.ParameterBandsPersistenceEmbeddable;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables.RecipeApplicabilityPersistenceEmbeddable;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.RecipeParameterPersistenceEntity;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.RecipePersistenceEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Static assembler between recipe domain and persistence representations (aggregate + child parameters).
 */
public final class RecipePersistenceAssembler {

    private RecipePersistenceAssembler() {
    }

    public static Recipe toDomainFromPersistence(RecipePersistenceEntity entity) {
        if (entity == null) return null;
        return Recipe.reconstitute(
                entity.getId(),
                entity.getHvofSystemId(),
                entity.getRecipeNumber(),
                entity.getName(),
                entity.getPowderSpecification(),
                entity.getStatus(),
                entity.getApplicabilities().stream()
                        .map(a -> new RecipeApplicability(a.getComponentType(), a.getMachineModel(), a.getPosition()))
                        .toList(),
                entity.getParameters().stream()
                        .map(RecipePersistenceAssembler::toDomainFromPersistence)
                        .toList());
    }

    public static RecipePersistenceEntity toPersistenceFromDomain(Recipe recipe) {
        if (recipe == null) return null;
        var entity = new RecipePersistenceEntity();
        if (recipe.getId() != null) {
            entity.setId(recipe.getId());
        }
        entity.setHvofSystemId(recipe.getHvofSystemId());
        entity.setRecipeNumber(recipe.getRecipeNumber());
        entity.setName(recipe.getName());
        entity.setPowderSpecification(recipe.getPowderSpecification());
        entity.setStatus(recipe.getStatus());
        entity.setApplicabilities(recipe.getApplicabilities().stream()
                .map(a -> new RecipeApplicabilityPersistenceEmbeddable(a.componentType(), a.machineModel(), a.position()))
                .collect(Collectors.toCollection(ArrayList::new)));
        entity.setParameters(recipe.getParameters().stream()
                .map(p -> toPersistenceFromDomain(p, entity))
                .collect(Collectors.toCollection(ArrayList::new)));
        return entity;
    }

    private static RecipeParameter toDomainFromPersistence(RecipeParameterPersistenceEntity e) {
        var b = e.getBands();
        return RecipeParameter.reconstitute(
                e.getId(),
                e.getParameter(),
                e.getSetpoint(),
                new ParameterBands(b.getLowerShutdown(), b.getLowerWarning(), b.getNominalMin(), b.getNominalMax(), b.getUpperWarning(), b.getUpperShutdown()),
                e.getUnitSymbol(),
                e.getUnitCategory());
    }

    private static RecipeParameterPersistenceEntity toPersistenceFromDomain(RecipeParameter p, RecipePersistenceEntity recipe) {
        var e = new RecipeParameterPersistenceEntity();
        if (p.getId() != null) {
            e.setId(p.getId());
        }
        e.setRecipe(recipe);
        e.setParameter(p.getParameter());
        e.setSetpoint(p.getSetpoint());
        var b = p.getBands();
        e.setBands(new ParameterBandsPersistenceEmbeddable(b.lowerShutdown(), b.lowerWarning(), b.nominalMin(), b.nominalMax(), b.upperWarning(), b.upperShutdown()));
        e.setUnitSymbol(p.getUnitSymbol());
        e.setUnitCategory(p.getUnitCategory());
        return e;
    }
}
