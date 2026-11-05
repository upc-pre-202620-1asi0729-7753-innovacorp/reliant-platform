package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeStatus;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.RecipePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipePersistenceRepository extends JpaRepository<RecipePersistenceEntity, Long> {

    @Query("select r from RecipePersistenceEntity r where r.hvofSystemId = :hvofSystemId order by r.recipeNumber, r.status")
    List<RecipePersistenceEntity> findAllByHvofSystemId(@Param("hvofSystemId") HvofSystemId hvofSystemId);

    @Query("select count(r) from RecipePersistenceEntity r where r.hvofSystemId = :hvofSystemId and r.recipeNumber = :recipeNumber and r.status = :status")
    long countByHvofSystemIdAndRecipeNumberAndStatus(@Param("hvofSystemId") HvofSystemId hvofSystemId,
                                                     @Param("recipeNumber") Integer recipeNumber,
                                                     @Param("status") RecipeStatus status);

    @Query("select r from RecipePersistenceEntity r where r.hvofSystemId = :hvofSystemId and r.recipeNumber = :recipeNumber and r.status = :status")
    Optional<RecipePersistenceEntity> findFirstByHvofSystemIdAndRecipeNumberAndStatus(@Param("hvofSystemId") HvofSystemId hvofSystemId,
                                                                                      @Param("recipeNumber") Integer recipeNumber,
                                                                                      @Param("status") RecipeStatus status);
}
