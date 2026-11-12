package com.innovacorp.reliant.platform.faultdiagnosis.application.internal.commandservices;

import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultCaseCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultRuleCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.AcknowledgeFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.OpenFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.ResolveFaultCaseCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.SeedDefaultFaultRulesCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.UserId;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultCaseRepository;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultRuleRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FaultCaseCommandServiceImpl implements FaultCaseCommandService {
    private final FaultCaseRepository caseRepository;
    private final FaultRuleRepository ruleRepository;
    private final FaultRuleCommandService ruleCommandService;
    private final CurrentUserProvider currentUserProvider;

    public FaultCaseCommandServiceImpl(FaultCaseRepository caseRepository, FaultRuleRepository ruleRepository,
                                       FaultRuleCommandService ruleCommandService, CurrentUserProvider currentUserProvider) {
        this.caseRepository = caseRepository;
        this.ruleRepository = ruleRepository;
        this.ruleCommandService = ruleCommandService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Optional<FaultCase> handle(OpenFaultCaseCommand command) {
        var organizationId = new OrganizationId(command.organizationId());
        var band = FaultBand.fromString(command.band());
        var spraySessionId = new SpraySessionId(command.spraySessionId());

        ruleCommandService.handle(new SeedDefaultFaultRulesCommand(command.organizationId()));
        var rule = ruleRepository.findByOrganizationIdAndParameterAndBand(organizationId, command.parameter(), band);
        if (rule.isEmpty()) return Optional.empty();

        var latest = caseRepository.findLatestBySpraySessionIdAndParameterAndBand(spraySessionId, command.parameter(), band);
        var cooldownMillis = rule.get().getCooldownSeconds() * 1000L;
        if (latest.isPresent() && command.epochMillis() - latest.get().getEpochMillis() < cooldownMillis) return Optional.empty();

        var faultCase = FaultCase.open(organizationId, spraySessionId, new HvofSystemId(command.hvofSystemId()), command.parameter(), band,
                command.value(), command.epochMillis(), rule.get());
        return Optional.of(caseRepository.save(faultCase));
    }

    @Override
    public Result<FaultCase, ApplicationError> handle(AcknowledgeFaultCaseCommand command) {
        var currentUser = currentUserProvider.current();
        var faultCase = caseRepository.findById(command.faultCaseId()).filter(c -> c.isOwnedBy(currentUser.organizationId()));
        if (faultCase.isEmpty()) {
            return Result.failure(ApplicationError.notFound("fault-case", String.valueOf(command.faultCaseId())));
        }
        try {
            faultCase.get().acknowledge(new UserId(currentUser.userId()));
            return Result.success(caseRepository.save(faultCase.get()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("fault-case-acknowledgement", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Fault case acknowledgement", e.getMessage()));
        }
    }

    @Override
    public Result<FaultCase, ApplicationError> handle(ResolveFaultCaseCommand command) {
        var currentUser = currentUserProvider.current();
        var faultCase = caseRepository.findById(command.faultCaseId()).filter(c -> c.isOwnedBy(currentUser.organizationId()));
        if (faultCase.isEmpty()) {
            return Result.failure(ApplicationError.notFound("fault-case", String.valueOf(command.faultCaseId())));
        }
        try {
            faultCase.get().resolve(new UserId(currentUser.userId()), command.resolutionNotes());
            return Result.success(caseRepository.save(faultCase.get()));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("fault-case-resolution", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Fault case resolution", e.getMessage()));
        }
    }
}
