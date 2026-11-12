package com.innovacorp.reliant.platform.processmonitoring.application.internal.commandservices;

import com.innovacorp.reliant.platform.processmonitoring.application.commandservices.SpraySessionCommandService;
import com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl.ExternalEquipmentService;
import com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl.ExternalTraceabilityService;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.*;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.ProcessReadingRepository;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.SpraySessionRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUser;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SpraySessionCommandServiceImpl implements SpraySessionCommandService {
    private final SpraySessionRepository sessionRepository;
    private final ProcessReadingRepository readingRepository;
    private final ExternalEquipmentService externalEquipmentService;
    private final ExternalTraceabilityService externalTraceabilityService;
    private final CurrentUserProvider currentUserProvider;

    public SpraySessionCommandServiceImpl(SpraySessionRepository sessionRepository, ProcessReadingRepository readingRepository,
                                          ExternalEquipmentService externalEquipmentService, ExternalTraceabilityService externalTraceabilityService,
                                          CurrentUserProvider currentUserProvider) {
        this.sessionRepository = sessionRepository;
        this.readingRepository = readingRepository;
        this.externalEquipmentService = externalEquipmentService;
        this.externalTraceabilityService = externalTraceabilityService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @Transactional
    public Result<SpraySession, ApplicationError> handle(StartSpraySessionCommand command) {
        var currentUser = currentUserProvider.current();
        var systemOrganizationId = externalEquipmentService.fetchHvofSystemOrganizationId(command.hvofSystemId());
        if (systemOrganizationId == 0L) {
            return Result.failure(ApplicationError.notFound("hvof-system", String.valueOf(command.hvofSystemId())));
        }
        if (!systemOrganizationId.equals(currentUser.organizationId())) {
            return Result.failure(forbidden("The HVOF system belongs to another organization"));
        }
        var recuperationOrganizationId = externalTraceabilityService.fetchRecuperationSupplierOrganizationId(command.recuperationId());
        if (recuperationOrganizationId == 0L || !recuperationOrganizationId.equals(currentUser.organizationId())) {
            return Result.failure(ApplicationError.notFound("recuperation", String.valueOf(command.recuperationId())));
        }
        var bands = externalEquipmentService.fetchActiveRecipeBands(command.hvofSystemId(), command.recipeNumber());
        if (bands.isEmpty()) {
            return Result.failure(ApplicationError.businessRuleViolation("active-recipe", "Recipe " + command.recipeNumber() + " is not ACTIVE for this HVOF system"));
        }
        try {
            var session = new SpraySession(new OrganizationId(currentUser.organizationId()), new HvofSystemId(command.hvofSystemId()),
                    new RecuperationId(command.recuperationId()), currentUser.userId(), command.recipeNumber(), command.timeZone(), bands);
            var saved = sessionRepository.save(session);
            externalTraceabilityService.linkSessionToRecuperation(command.recuperationId(), saved.getId());
            return Result.success(saved);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("SpraySession", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Spray session start", e.getMessage()));
        }
    }

    @Override
    public Result<SpraySession, ApplicationError> handle(CompleteSpraySessionCommand command) {
        return transition(command.spraySessionId(), session -> session.complete(), "Spray session completion");
    }

    @Override
    public Result<SpraySession, ApplicationError> handle(AbortSpraySessionCommand command) {
        return transition(command.spraySessionId(), session -> session.abort(command.reason()), "Spray session abortion");
    }

    @Override
    @Transactional
    public Result<List<ProcessReading>, ApplicationError> handle(IngestProcessReadingsCommand command) {
        var owned = ownedActiveSession(command.spraySessionId());
        if (owned.isFailure()) return Result.failure(((Result.Failure<SpraySession, ApplicationError>) owned).error());
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();
        try {
            var readings = command.readings().stream()
                    .map(r -> session.classify(r.epochMillis(), r.plcClockOffsetMillis() == null ? 0L : r.plcClockOffsetMillis(), r.tagPath(), r.parameter(),
                            r.subsystemId(), r.partId(), r.value()))
                    .toList();
            var saved = readingRepository.saveAll(readings);
            sessionRepository.save(session);   // publishes OutOfBandReadingDetectedEvent(s) registered by classify()
            return Result.success(saved);
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("session-status", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("ProcessReading", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Readings ingestion", e.getMessage()));
        }
    }

    @Override
    public Result<SprayPass, ApplicationError> handle(RegisterSprayPassCommand command) {
        var owned = ownedActiveSession(command.spraySessionId());
        if (owned.isFailure()) return Result.failure(((Result.Failure<SpraySession, ApplicationError>) owned).error());
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();
        try {
            var pass = session.registerPass(command.passNumber(), command.startedAt(), command.endedAt());
            var saved = sessionRepository.save(session);
            return saved.getPasses().stream().filter(p -> p.getPassNumber() == pass.getPassNumber()).findFirst()
                    .<Result<SprayPass, ApplicationError>>map(Result::success)
                    .orElseGet(() -> Result.failure(ApplicationError.unexpected("pass-registration", "Pass not found after save")));
        } catch (IllegalStateException e) {
            return e.getMessage().contains("already exists")
                    ? Result.failure(ApplicationError.conflict("spray-pass", String.valueOf(command.passNumber())))
                    : Result.failure(ApplicationError.businessRuleViolation("session-status", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("SprayPass", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("Pass registration", e.getMessage()));
        }
    }

    private Result<SpraySession, ApplicationError> transition(Long spraySessionId, java.util.function.Consumer<SpraySession> action, String context) {
        var owned = ownedSession(spraySessionId);
        if (owned.isFailure()) return owned;
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();
        try {
            action.accept(session);
            return Result.success(sessionRepository.save(session));
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("session-status", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("SpraySession", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected(context, e.getMessage()));
        }
    }

    private Result<SpraySession, ApplicationError> ownedSession(Long spraySessionId) {
        CurrentUser currentUser = currentUserProvider.current();
        Optional<SpraySession> session = sessionRepository.findById(spraySessionId);
        if (session.isEmpty() || !session.get().getOrganizationIdValue().equals(currentUser.organizationId())) {
            return Result.failure(ApplicationError.notFound("spray-session", String.valueOf(spraySessionId)));
        }
        return Result.success(session.get());
    }

    private Result<SpraySession, ApplicationError> ownedActiveSession(Long spraySessionId) {
        var owned = ownedSession(spraySessionId);
        if (owned.isFailure()) return owned;
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();
        if (!session.isActive()) {
            return Result.failure(ApplicationError.businessRuleViolation("session-status", "The spray session is not active"));
        }
        return owned;
    }

    private static ApplicationError forbidden(String reason) {
        return new ApplicationError("ACCESS_FORBIDDEN", "Access forbidden", reason);
    }
}
