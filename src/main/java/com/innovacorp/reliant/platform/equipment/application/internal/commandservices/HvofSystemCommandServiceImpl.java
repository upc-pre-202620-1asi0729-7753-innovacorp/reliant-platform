package com.innovacorp.reliant.platform.equipment.application.internal.commandservices;

import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandService;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterControllerCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSubsystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.DeviceType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.NetworkEndpoint;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.Protocol;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.SubsystemType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.domain.repositories.HvofSystemRepository;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Result<HvofSubsystem, ApplicationError> handle(RegisterHvofSubsystemCommand command) {
        var hvofSystem = hvofSystemRepository.findById(command.hvofSystemId());
        if (hvofSystem.isEmpty()) {
            return Result.failure(ApplicationError.notFound("hvof-system", String.valueOf(command.hvofSystemId())));
        }
        try {
            var subsystem = hvofSystem.get().addSubsystem(
                    SubsystemType.fromString(command.subsystemType()),
                    command.name(),
                    command.alias(),
                    command.parameters()
            );
            var saved = hvofSystemRepository.save(hvofSystem.get());
            return saved.getSubsystems().stream()
                    .filter(s -> s.getName().equals(subsystem.getName()))
                    .findFirst()
                    .<Result<HvofSubsystem, ApplicationError>>map(Result::success)
                    .orElseGet(() -> Result.failure(ApplicationError.unexpected(
                            "subsystem-registration", "Subsystem not found after save")));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.conflict("hvof-subsystem", command.name()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("HvofSubsystem", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("subsystem-registration", e.getMessage()));
        }
    }

    @Override
    public Result<Controller, ApplicationError> handle(RegisterControllerCommand command) {
        var hvofSystem = hvofSystemRepository.findById(command.hvofSystemId());
        if (hvofSystem.isEmpty()) {
            return Result.failure(ApplicationError.notFound("hvof-system", String.valueOf(command.hvofSystemId())));
        }
        try {
            var protocols = command.supportedProtocols() == null
                    ? List.<Protocol>of()
                    : command.supportedProtocols().stream().map(Protocol::fromString).toList();

            var controller = hvofSystem.get().addController(
                    command.controllerNumber(),
                    DeviceType.fromString(command.deviceType()),
                    command.serialNumber(),
                    command.manufacturer(),
                    command.model(),
                    command.partNumber(),
                    new NetworkEndpoint(command.ipAddress(), command.port(), command.rack(), command.slot()),
                    command.endpointUrl(),
                    command.tagCatalogId(),
                    protocols
            );

            var saved = hvofSystemRepository.save(hvofSystem.get());
            return saved.getControllers().stream()
                    .filter(c -> c.getControllerNumber() == controller.getControllerNumber())
                    .findFirst()
                    .<Result<Controller, ApplicationError>>map(Result::success)
                    .orElseGet(() -> Result.failure(ApplicationError.unexpected(
                            "controller-registration", "Controller not found after save")));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.conflict("controller", String.valueOf(command.controllerNumber())));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Controller", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("controller-registration", e.getMessage()));
        }
    }
}