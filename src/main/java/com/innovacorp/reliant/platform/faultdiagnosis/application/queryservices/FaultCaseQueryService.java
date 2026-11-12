package com.innovacorp.reliant.platform.faultdiagnosis.application.queryservices;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetAllFaultCasesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetFaultCaseByIdQuery;

import java.util.List;
import java.util.Optional;

public interface FaultCaseQueryService {
    List<FaultCase> handle(GetAllFaultCasesByOrganizationIdQuery query);

    Optional<FaultCase> handle(GetFaultCaseByIdQuery query);
}
