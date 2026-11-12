package com.innovacorp.reliant.platform.equipment.interfaces.acl;

import java.util.List;

public interface EquipmentContextFacade {

    /**
     * Returns the owning organization ID, or zero when the HVOF system does not exist.
     */
    Long fetchHvofSystemOrganizationId(Long hvofSystemId);

    /**
     * Returns the bands of the active recipe, or an empty list when no matching recipe is active.
     */
    List<RecipeBandSnapshot> fetchActiveRecipeBands(Long hvofSystemId, Integer recipeNumber);
}