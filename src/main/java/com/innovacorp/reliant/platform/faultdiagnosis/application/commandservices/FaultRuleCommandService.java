package com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.CreateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.SeedDefaultFaultRulesCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.UpdateFaultRuleCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

public interface FaultRuleCommandService {
    Result<FaultRule, ApplicationError> handle(CreateFaultRuleCommand command);

    Result<FaultRule, ApplicationError> handle(UpdateFaultRuleCommand command);

    void handle(SeedDefaultFaultRulesCommand command);
}
