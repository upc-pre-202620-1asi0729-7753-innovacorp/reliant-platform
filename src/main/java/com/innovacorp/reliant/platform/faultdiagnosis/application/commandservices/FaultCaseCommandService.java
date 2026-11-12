package com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.AcknowledgeFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.OpenFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.ResolveFaultCaseCommand;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;

import java.util.Optional;

public interface FaultCaseCommandService {
    /** Empty when no rule matches or the cooldown is active. */
    Optional<FaultCase> handle(OpenFaultCaseCommand command);

    Result<FaultCase, ApplicationError> handle(AcknowledgeFaultCaseCommand command);

    Result<FaultCase, ApplicationError> handle(ResolveFaultCaseCommand command);
}
