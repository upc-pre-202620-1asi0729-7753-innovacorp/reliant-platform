package com.innovacorp.reliant.platform.equipment.application.internal.commandservices;

import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.domain.repositories.HvofSystemRepository;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;

/**
 * HVOF System Command Service Implementation
 */
@Service
public class HvofSystemCommandServiceImpl implements HvofSystemCommandService {
    private final HvofSystemRepository hvofSystemRepository;

    public HvofSystemCommandServiceImpl(HvofSystemRepository hvofSystemRepository) {
        this.hvofSystemRepository = hvofSystemRepository;
    }

    @Override
    public Result<HvofSystem, ApplicationError> handle(RegisterHvofSystemCommand command) {
        try {
            var organizationId = new OrganizationId(command.organizationId());
            var code = new HvofSystemCode(command.code());
            if (hvofSystemRepository.existsByOrganizationIdAndCode(organizationId, code)) {
                return Result.failure(ApplicationError.conflict("hvof-system", command.code()));
            }
            var hvofSystem = new HvofSystem(command);
            return Result.success(hvofSystemRepository.save(hvofSystem));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("HvofSystem", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("HVOF system registration", e.getMessage()));
        }
    }
}