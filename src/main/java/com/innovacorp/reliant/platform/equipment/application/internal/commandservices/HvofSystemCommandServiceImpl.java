package com.innovacorp.reliant.platform.equipment.application.internal.commandservices;

import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandFailure;
import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.repositories.HvofSystemRepository;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem.ORGANIZATION_CODE_UNIQUE_CONSTRAINT;

@Slf4j
@Service
public class HvofSystemCommandServiceImpl implements HvofSystemCommandService {
    private final HvofSystemRepository hvofSystemRepository;

    public HvofSystemCommandServiceImpl(HvofSystemRepository hvofSystemRepository) {
        this.hvofSystemRepository = hvofSystemRepository;
    }

    @Override
    @Transactional
    public Result<HvofSystem, HvofSystemCommandFailure> handle(RegisterHvofSystemCommand command) {
        if (hvofSystemRepository.existsByOrganizationIdAndCode(command.organizationId(), command.code())) {
            log.warn("Duplicate HVOF system (pre-check): organizationId={}, code={}", command.organizationId().organizationId(), command.code().code());
            return Result.failure(new HvofSystemCommandFailure.Duplicate());
        }
        try {
            var created = hvofSystemRepository.save(new HvofSystem(command));
            log.info("HVOF system registered: id={}, organizationId={}, code={}", created.getId(), created.getOrganizationIdValue(), created.getCodeValue());
            return Result.success(created);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateViolation(exception)) {
                log.warn("Duplicate HVOF system (constraint): organizationId={}, code={}", command.organizationId().organizationId(), command.code().code());
                return Result.failure(new HvofSystemCommandFailure.Duplicate());
            }
            throw exception;
        }
    }

    private boolean isDuplicateViolation(DataIntegrityViolationException exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause.getMessage() != null && cause.getMessage().contains(ORGANIZATION_CODE_UNIQUE_CONSTRAINT)) return true;
            cause = cause.getCause();
        }
        return false;
    }
}