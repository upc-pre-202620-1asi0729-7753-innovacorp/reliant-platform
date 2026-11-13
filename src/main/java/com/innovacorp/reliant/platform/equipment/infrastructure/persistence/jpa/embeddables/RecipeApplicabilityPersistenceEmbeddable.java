package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence representation of RecipeApplicability (element collection row).
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeApplicabilityPersistenceEmbeddable {

    @Column(name = "component_type", nullable = false, length = 40)
    private String componentType;

    @Column(name = "machine_model", nullable = false, length = 80)
    private String machineModel;

    @Column(name = "position", nullable = false, length = 60)
    private String position;
}
