package com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl;

import com.innovacorp.reliant.platform.equipment.interfaces.acl.EquipmentContextFacade;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecipeBand;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .map(snapshot -> new RecipeBand(
                        snapshot.parameter(), snapshot.setpoint(),
                        snapshot.lowerShutdown(), snapshot.lowerWarning(),
                        snapshot.nominalMin(), snapshot.nominalMax(),
                        snapshot.upperWarning(), snapshot.upperShutdown(),
                        snapshot.unitSymbol(), snapshot.unitCategory()))
                .toList();
    }
}