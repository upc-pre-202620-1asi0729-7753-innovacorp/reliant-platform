package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.UnitCategory;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables.ParameterBandsPersistenceEmbeddable;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "recipe_parameters", uniqueConstraints = @UniqueConstraint(name = "uk_recipe_parameter", columnNames = {"recipe_id", "parameter"}))
@Getter
@Setter
@NoArgsConstructor
public class RecipeParameterPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipePersistenceEntity recipe;
    @Column(name = "parameter", nullable = false, length = 40)
    private String parameter;
    @Column(name = "setpoint", nullable = false)
    private double setpoint;
    @Embedded
    private ParameterBandsPersistenceEmbeddable bands;
    @Column(name = "unit_symbol", nullable = false, length = 10)
    private String unitSymbol;
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_category", nullable = false, length = 20)
    private UnitCategory unitCategory;
}
