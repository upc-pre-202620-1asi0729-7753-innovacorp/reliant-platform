package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.RecipeStatus;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.converters.HvofSystemIdPersistenceConverter;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables.RecipeApplicabilityPersistenceEmbeddable;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes", indexes = @Index(name = "ix_recipe_system_number_status", columnList = "hvof_system_id, recipe_number, status"))
@Getter
@Setter
@NoArgsConstructor
public class RecipePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = HvofSystemIdPersistenceConverter.class)
    @Column(name = "hvof_system_id", nullable = false)
    private HvofSystemId hvofSystemId;

    @Column(name = "recipe_number", nullable = false)
    private Integer recipeNumber;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "powder_specification", length = 120)
    private String powderSpecification;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecipeStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_applicabilities", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<RecipeApplicabilityPersistenceEmbeddable> applicabilities = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeParameterPersistenceEntity> parameters = new ArrayList<>();
}
