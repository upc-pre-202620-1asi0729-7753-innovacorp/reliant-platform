package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.adapters;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.Recipe;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeStatus;
import com.innovacorp.reliant.platform.equipment.domain.repositories.RecipeRepository;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.assemblers.RecipePersistenceAssembler;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories.RecipePersistenceRepository;
import com.innovacorp.reliant.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecipeRepositoryImpl implements RecipeRepository {

    private final RecipePersistenceRepository persistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public RecipeRepositoryImpl(RecipePersistenceRepository persistenceRepository, DomainEventPublisher domainEventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Recipe save(Recipe recipe) {
        var savedEntity = persistenceRepository.save(RecipePersistenceAssembler.toPersistenceFromDomain(recipe));
        var saved = RecipePersistenceAssembler.toDomainFromPersistence(savedEntity);
        // Events registered before save (e.g. publish()) live on the incoming aggregate; publish both.
        domainEventPublisher.publishAndClearEvents(recipe);
        domainEventPublisher.publishAndClearEvents(saved);
        return saved;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return persistenceRepository.findById(id).map(RecipePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Recipe> findAllByHvofSystemId(HvofSystemId hvofSystemId) {
        return persistenceRepository.findAllByHvofSystemId(hvofSystemId).stream()
                .map(RecipePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByHvofSystemIdAndRecipeNumberAndStatus(HvofSystemId hvofSystemId, Integer recipeNumber, RecipeStatus status) {
        return persistenceRepository.countByHvofSystemIdAndRecipeNumberAndStatus(hvofSystemId, recipeNumber, status) > 0;
    }

    @Override
    public Optional<Recipe> findActiveByHvofSystemIdAndRecipeNumber(HvofSystemId hvofSystemId, Integer recipeNumber) {
        return persistenceRepository.findFirstByHvofSystemIdAndRecipeNumberAndStatus(hvofSystemId, recipeNumber, RecipeStatus.ACTIVE)
                .map(RecipePersistenceAssembler::toDomainFromPersistence);
    }
}
