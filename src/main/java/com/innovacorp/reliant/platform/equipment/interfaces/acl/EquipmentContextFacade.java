package com.innovacorp.reliant.platform.equipment.interfaces.acl;

import java.util.List;

/**
 * ACL facade that exposes Equipment capabilities to other bounded contexts.
 */
public interface EquipmentContextFacade {
    /** @return the owning organization id of the HVOF system, or 0L when it does not exist. */
    Long fetchHvofSystemOrganizationId(Long hvofSystemId);

    /** @return the parameters of the ACTIVE recipe with that number, or an empty list when none is active. */
    List<RecipeBandSnapshot> fetchActiveRecipeBands(Long hvofSystemId, Integer recipeNumber);
}
