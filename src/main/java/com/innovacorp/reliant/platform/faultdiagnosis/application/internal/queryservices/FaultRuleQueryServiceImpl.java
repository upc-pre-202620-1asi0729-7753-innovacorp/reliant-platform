package com.innovacorp.reliant.platform.faultdiagnosis.application.internal.queryservices;

import com.innovacorp.reliant.platform.faultdiagnosis.application.queryservices.FaultRuleQueryService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.queries.GetAllFaultRulesByOrganizationIdQuery;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultRuleQueryServiceImpl implements FaultRuleQueryService {
    private final FaultRuleRepository ruleRepository;

    public FaultRuleQueryServiceImpl(FaultRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public List<FaultRule> handle(GetAllFaultRulesByOrganizationIdQuery query) {
        return ruleRepository.findAllByOrganizationId(query.organizationId());
    }
}
