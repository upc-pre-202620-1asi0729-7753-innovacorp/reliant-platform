package com.innovacorp.reliant.platform.faultdiagnosis.application.internal.commandservices;

import com.innovacorp.reliant.platform.faultdiagnosis.application.commandservices.FaultRuleCommandService;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.CreateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.SeedDefaultFaultRulesCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.UpdateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.repositories.FaultRuleRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaultRuleCommandServiceImpl implements FaultRuleCommandService {
    private static final int DEFAULT_COOLDOWN_SECONDS = 60;

    /** FESA defaults: parameter, band, probable cause, recommended action. */
    private static final List<String[]> DEFAULT_RULES = List.of(
            new String[]{"fuel_gas_flow", "SHUTDOWN", "Fuel line pressure drop or regulator failure", "Stop spraying; inspect fuel regulator and supply pressure"},
            new String[]{"fuel_gas_flow", "WARNING", "Partial obstruction or drifting mass flow controller", "Check MFC calibration before next pass"},
            new String[]{"oxygen_flow", "SHUTDOWN", "Oxygen supply interruption", "Stop spraying; verify O2 manifold and valve"},
            new String[]{"oxygen_flow", "WARNING", "O2/fuel ratio drifting", "Review combustion ratio setpoint"},
            new String[]{"powder_feed_rate", "SHUTDOWN", "Hopper empty or feed line clogged", "Stop; refill hopper / clear feed line"},
            new String[]{"powder_feed_rate", "WARNING", "Powder humidity or carrier gas fluctuation", "Check powder lot and carrier gas flow"},
            new String[]{"carrier_gas_flow", "WARNING", "Carrier gas leak or regulator drift", "Inspect carrier gas line fittings"},
            new String[]{"coolant_outlet_temp", "SHUTDOWN", "Coolant flow loss or chiller failure", "Stop; check chiller and coolant pump"},
            new String[]{"coolant_outlet_temp", "WARNING", "Reduced coolant flow", "Clean coolant filter; verify flow"},
            new String[]{"coolant_flow", "SHUTDOWN", "Coolant pump failure", "Stop; inspect pump and hoses"},
            new String[]{"hopper_pressure", "WARNING", "Hopper seal leak", "Inspect hopper lid seal"},
            new String[]{"spindle_speed", "WARNING", "Spindle drive load variation", "Check part balance and chuck"});

    private final FaultRuleRepository ruleRepository;
    private final CurrentUserProvider currentUserProvider;

    public FaultRuleCommandServiceImpl(FaultRuleRepository ruleRepository, CurrentUserProvider currentUserProvider) {
        this.ruleRepository = ruleRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result<FaultRule, ApplicationError> handle(CreateFaultRuleCommand command) {
        var currentUser = currentUserProvider.current();
        if (!currentUser.hasRole("ROLE_ORG_ADMIN")) {
            return Result.failure(new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only organization administrators manage fault rules"));
        }
        try {
            var organizationId = new OrganizationId(currentUser.organizationId());
            var band = FaultBand.fromString(command.band());
            if (ruleRepository.findByOrganizationIdAndParameterAndBand(organizationId, command.parameter(), band).isPresent()) {
                return Result.failure(ApplicationError.conflict("fault-rule", command.parameter() + "/" + band.name().toLowerCase()));
            }
            var rule = new FaultRule(new CreateFaultRuleCommand(currentUser.organizationId(), command.parameter(), command.band(),
                    command.probableCause(), command.recommendedAction(), command.cooldownSeconds()));
            return Result.success(ruleRepository.save(rule));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("FaultRule", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Fault rule creation", e.getMessage()));
        }
    }

    @Override
    public Result<FaultRule, ApplicationError> handle(UpdateFaultRuleCommand command) {
        var currentUser = currentUserProvider.current();
        var rule = ruleRepository.findById(command.faultRuleId()).filter(r -> r.isOwnedBy(currentUser.organizationId()));
        if (rule.isEmpty()) {
            return Result.failure(ApplicationError.notFound("fault-rule", String.valueOf(command.faultRuleId())));
        }
        if (!currentUser.hasRole("ROLE_ORG_ADMIN")) {
            return Result.failure(new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", "Only organization administrators manage fault rules"));
        }
        try {
            rule.get().update(command);
            return Result.success(ruleRepository.save(rule.get()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("FaultRule", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Fault rule update", e.getMessage()));
        }
    }

    /** Idempotent: seeds the FESA defaults only when the organization has no rules yet. */
    @Override
    public void handle(SeedDefaultFaultRulesCommand command) {
        var organizationId = new OrganizationId(command.organizationId());
        if (ruleRepository.existsByOrganizationId(organizationId)) return;
        DEFAULT_RULES.forEach(r -> ruleRepository.save(
                new FaultRule(null, organizationId, r[0], FaultBand.valueOf(r[1]), r[2], r[3], DEFAULT_COOLDOWN_SECONDS)));
    }
}
