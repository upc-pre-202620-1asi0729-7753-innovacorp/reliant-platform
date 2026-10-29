package com.innovacorp.reliant.platform.equipment.application.queryservices;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetAllHvofSystemsByOrganizationIdQuery;
import com.innovacorp.reliant.platform.equipment.domain.model.queries.GetHvofSystemByIdQuery;

import java.util.List;
import java.util.Optional;

public interface HvofSystemQueryService {
    Optional<HvofSystem> handle(GetHvofSystemByIdQuery query);
    List<HvofSystem> handle(GetAllHvofSystemsByOrganizationIdQuery query);
}