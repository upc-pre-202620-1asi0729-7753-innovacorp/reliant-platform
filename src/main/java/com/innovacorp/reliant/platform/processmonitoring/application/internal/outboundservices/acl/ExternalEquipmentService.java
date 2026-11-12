package com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl;

import com.innovacorp.reliant.platform.equipment.interfaces.acl.EquipmentContextFacade;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecipeBand;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Wraps the Equipment ACL facade and translates its snapshots into this context's value objects.
 */
@Service
public class ExternalEquipmentService {
    private final EquipmentContextFacade equipmentContextFacade;

    public ExternalEquipmentService(EquipmentContextFacade equipmentContextFacade) {
        this.equipmentContextFacade = equipmentContextFacade;
    }

    public Long fetchHvofSystemOrganizationId(Long hvofSystemId) {
        return equipmentContextFacade.fetchHvofSystemOrganizationId(hvofSystemId);
    }

    public List<RecipeBand> fetchActiveRecipeBands(Long hvofSystemId, Integer recipeNumber) {
        return equipmentContextFacade.fetchActiveRecipeBands(hvofSystemId, recipeNumber).stream()
                .map(s -> new RecipeBand(s.parameter(), s.setpoint(), s.lowerShutdown(), s.lowerWarning(), s.nominalMin(), s.nominalMax(),
                        s.upperWarning(), s.upperShutdown(), s.unitSymbol(), s.unitCategory()))
                .toList();
    }
}
