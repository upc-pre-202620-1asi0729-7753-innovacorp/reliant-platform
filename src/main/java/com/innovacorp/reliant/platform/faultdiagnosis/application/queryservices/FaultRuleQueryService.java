package com.innovacorp.reliant.platform.faultdiagnosis.application.queryservices;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetAllFaultRulesByOrganizationIdQuery;

import java.util.List;

public interface FaultRuleQueryService {
    List<FaultRule> handle(GetAllFaultRulesByOrganizationIdQuery query);
}
